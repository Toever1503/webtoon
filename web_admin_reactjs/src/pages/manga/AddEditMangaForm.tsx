import React, { ChangeEvent, ChangeEventHandler, EventHandler, useEffect, useRef, useState } from 'react';
import { Button, Select, Table, TablePaginationConfig, Tag, Input, DatePicker, Divider, Space, InputRef, Tooltip, Checkbox } from 'antd';
import MangaChapterForm from './component/MangaChapterForm';
import { ArrowLeftOutlined, PlusOutlined } from '@ant-design/icons';
import debounce from '../../utils/debounce';
import RichtextEditorForm from '../../components/RichtextEditorForm';
import { RichTextEditorComponent } from '@syncfusion/ej2-react-richtexteditor';
import { useTranslation } from "react-i18next";
import dayjs from 'dayjs';
import mangaService, { MangaInput, MangaStatus, ReleaseStatus } from '../../services/manga/MangaService';
import { useDispatch } from 'react-redux';
import { showNofification } from '../../stores/features/notification/notificationSlice';
import { NavigateFunction, useNavigate, useParams } from 'react-router-dom';
import { AppDispatch } from '../../stores';
import tagService from '../../services/TagService';
import genreService, { GenreInput } from '../../services/manga/GenreService';
import GenreSelect from './component/manga-form-new/GenreSelect';
import AuthorSelect from './component/manga-form-new/AuthorSelect';
import TagSelect from './component/manga-form-new/TagSelect';
import { AuthorInput } from '../../services/manga/AuthorService';



export interface MangaInputError {
    title: string;
    description: string;
    featuredImage: string;
}

let isAutoSavingMangaInfo = false;
export async function autoSaveMangaInfo(mangaInput: MangaInput) {
    if (isAutoSavingMangaInfo) return;
    console.log('autoSaveMangaInfo: id ', mangaInput.id);

    isAutoSavingMangaInfo = true;
    try {
        const res = await mangaService.addMangaInfo(mangaInput);
        console.log('auto save manga success', res.data);
        mangaInput.id = res.data.id;
    }
    catch (err) {
        console.log('auto save manga failed');
        console.log(err);
    } finally {
        isAutoSavingMangaInfo = false;
    }

};

interface AddEditMangaFormProps {
    type: 'ADD' | 'EDIT';
}

const AddEditMangaForm: React.FC<AddEditMangaFormProps> = (props: AddEditMangaFormProps) => {
    const { t } = useTranslation();
    const dispatch: AppDispatch = useDispatch();
    const navigate: NavigateFunction = useNavigate();


    // manga info
    const [mangaInput, setMangaInput] = useState<MangaInput>({
        id: '',
        title: 'test',
        description: 'The Rich Text Editor component is WYSIWYG ("what you see is what you get") editor that provides the best user experience to create and update the content. Users can format their content using standard toolbar commands.',
        excerpt: '',
        mangaName: '',
        status: 'DRAFTED',
        mangaStatus: 'COMING',
        releaseYear: dayjs().format('YYYY'),
        mangaType: 'UNSET',
        genres: [],
        authors: [],
        tags: [],
        isFree: false,
        featuredImage: 'https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAVSp94.img?w=768&h=1024&m=6&x=257&y=190&s=273&d=273',
        displayType: 'CHAP'
    });
    const [isSavingMangaInfo, setIsSavingMangaInfo] = useState<boolean>(false);
    const [mangaContentEditorRef, setMangaContentEditorRef] = useState<RichTextEditorComponent>();
    const onReadyMangaContentEditor = (rteObj: RichTextEditorComponent, setContent: Function) => {
        setMangaContentEditorRef(rteObj);
        setContent(mangaInput.description);
    };

    const onSaveMangaInfo = async () => {
        if (isSavingMangaInfo)
            return;

        const mangaContent = mangaContentEditorRef?.getHtml() || '';
        const mangaExcerpt = mangaContentEditorRef?.getText().slice(0, 160 || '') || '';
        setMangaInput({
            ...mangaInput,
            description: mangaContent,
            excerpt: mangaExcerpt,
        });

        console.log('mangaInput', mangaInput);

        let errorCount = 0;
        if (!mangaInput.title) {
            errorCount++;
            mangaInputError.title = 'manga.form.errors.title-required';
        }
        else mangaInputError.title = '';

        if (!mangaInput.description) {
            errorCount++;
            mangaInputError.description = 'manga.form.errors.content-required';
        }
        else mangaInputError.description = '';


        if (!mangaInput.featuredImage) {
            errorCount++;
            mangaInputError.featuredImage = 'manga.form.errors.feature-image-required';
        }
        else mangaInputError.featuredImage = '';
        setMangaInputError({ ...mangaInputError });

        if (errorCount === 0) {
            console.log('begin save manga');

            setIsSavingMangaInfo(true);
            mangaInput.status = 'PUBLISHED';
            mangaService.addMangaInfo(mangaInput)
                .then((res) => {
                    console.log('add manga success', res.data);

                    if (props.type === 'ADD')
                        dispatch(showNofification({
                            type: 'success',
                            message: t('manga.form.errors.add-success'),
                        }));
                    else
                        dispatch(showNofification({
                            type: 'success',
                            message: t('manga.form.errors.edit-success'),
                        }));
                    navigate(`/mangas`);
                })
                .catch((err) => {
                    console.log(err);
                    if (props.type === 'ADD')
                        dispatch(showNofification({
                            type: 'success',
                            message: t('manga.form.errors.add-failed'),
                        }));
                    else
                        dispatch(showNofification({
                            type: 'success',
                            message: t('manga.form.errors.edit-failed'),
                        }));

                })
                .finally(() => {
                    setIsSavingMangaInfo(false);
                });
        }
        else {
            console.log(mangaInputError);

            dispatch(showNofification({
                type: 'error',
                message: t('manga.form.errors.check-again'),
            }))
        }
    };

    const [mangaInputError, setMangaInputError] = useState<MangaInputError>({
        title: '',
        description: '',
        featuredImage: '',
    });

    let { id } = useParams();
    const [isFetchingMangaInfo, setIsFetchingMangaInfo] = useState<boolean>(true);
    useEffect(() => {
        // let id: number | undefined;
        // id = setInterval(() => {
        //     autoSaveMangaInfo(mangaInput);
        // }, 15000);
        // return () => clearInterval(id);
        if (props.type === 'EDIT') {
            if (id) {
                mangaService.findById(id)
                    .then((res) => {
                        console.log('get manga success', res.data);

                        setMangaInput({
                            ...res.data,
                            originalAuthors: res.data.authors,
                            originalGenres: res.data.genres,
                            originalTags: res.data.tags,
                            authors: res.data.authors.map((author: AuthorInput) => author.id),
                            genres: res.data.genres.map((genre: GenreInput) => genre.id),
                            tags: res.data.tags.map((tag: GenreInput) => tag.id),
                        });
                    })
                    .catch((err) => {
                        console.log('get manga failed', err);
                        dispatch(showNofification({
                            type: 'error',
                            message: 'Có lỗi xảy ra, vui lòng thử lại!',
                        }));
                    })
                    .finally(() => setIsFetchingMangaInfo(false));
            }
        }
        else setIsFetchingMangaInfo(false);
    }, []);


    return (
        <div className="space-y-3 py-3">
            <div className="flex space-x-3 items-center">
                <Tooltip title="Quay lại">
                    <ArrowLeftOutlined onClick={() => navigate(-1)} />
                </Tooltip>
                <p className="text-[23px] font-[600] m-0">
                    {
                        props.type === 'ADD' ?
                            'Thêm mới truyện'
                            :
                            'Cập nhật truyện'
                    }
                </p>
                <Button loading={isSavingMangaInfo} onClick={onSaveMangaInfo}>
                    {t('buttons.save')}
                </Button>
            </div>

            {
                !isFetchingMangaInfo &&
                <div className='flex gap-[15px]'>
                    <div className=''>
                        <section className='border max-w-[1000px] h-fit'>
                            <label htmlFor="mangaTitle" className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px]'>
                                <span className='text-red-500'>*</span>
                                <span> Tên truyện:</span>
                            </label>
                            <Input id="mangaTitle" className='' value={mangaInput.title} onChange={(val: any) => setMangaInput({ ...mangaInput, title: val.target.value })} placeholder="Title" />
                            {
                                <p className='text-[12px] text-red-500 px-[5px]'>
                                    {mangaInputError.title && t(mangaInputError.title)}
                                </p>
                            }

                            <div className=''>
                                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px]'>
                                    <span className='text-red-500'>*</span>
                                    <span>Nội dung:</span>
                                </label>
                                <RichtextEditorForm onReady={onReadyMangaContentEditor} toolbarSettings={{}} />
                                {
                                    <p className='text-[12px] text-red-500 px-[5px]'>
                                        {mangaInputError.description && t(mangaInputError.description)}
                                    </p>
                                }
                            </div>
                        </section>

                        <MangaChapterForm mangaInput={mangaInput} formType={props.type} setMangaInput={setMangaInput} />
                    </div>

                    <div className='manga-form-sidebar w-[280px] grid gap-y-[15px]' >
                        <section className='bg-white grid gap-y-[10px] pb-[15px]' style={{ border: '1px solid #c3c4c7' }}>
                            <p className='text-[18px] font-bold py-[10px] px-[10px] m-0' style={{ borderBottom: '1px solid #c3c4c7' }}>Thông tin bổ sung</p>

                            <div className='flex gap-x-[15px] px-[10px] mt-[10px]'>
                                <span className='text-[14px] font-bold'>Truyện có phí?</span>
                                <Checkbox checked={mangaInput.isFree} onChange={e => mangaInput.isFree = e.target.checked}/>
                            </div>


                            {/* <div className='flex justify-between items-center px-[10px]'>
                                <span className='text-[14px] font-bold'>Trạng thái:</span>
                                <Select
                                    className='min-w-[150px]'
                                    value={mangaInput.status}
                                    onChange={(val: MangaStatus) => { setMangaInput({ ...mangaInput, status: val }) }}
                                    options={[
                                        { value: 'PUBLISHED', label: 'Đăng ngay' },
                                        { value: 'DRAFTED', label: 'Đăng sau' },
                                    ]}
                                />
                            </div> */}

                            <div className='flex justify-between items-center px-[10px]'>
                                <span className='text-[14px] font-bold'>Tình trạng phát hành:</span>
                                <Select
                                    className='min-w-[150px]'
                                    defaultValue="COMING"
                                    onChange={(val: ReleaseStatus) => { setMangaInput({ ...mangaInput, mangaStatus: val }) }}
                                    options={[
                                        { value: 'COMING', label: 'Sắp ra mắt' },
                                        { value: 'GOING', label: 'Đang ra' },
                                        { value: 'STOPPED', label: 'Đã tạm dừng' },
                                        { value: 'CANCELLED', label: 'Đã hủy' },
                                        { value: 'COMPLETED', label: 'Đã hoàn thành' },
                                    ]}
                                />
                            </div>

                            <div className='flex justify-between items-center px-[10px]'>
                                <span className='text-[14px] font-bold'>Năm phát hành:</span>
                                <DatePicker allowClear={false} mode='year' onChange={(val) => setMangaInput({ ...mangaInput, releaseYear: val?.format('YYYY') })}
                                    value={dayjs(`${mangaInput.releaseYear}-01-01`)} placement='bottomRight' picker="year" />
                            </div>

                            <GenreSelect mangaInput={mangaInput} mangaInputError={mangaInputError} />
                            <AuthorSelect mangaInput={mangaInput} mangaInputError={mangaInputError} />
                            <TagSelect mangaInput={mangaInput} mangaInputError={mangaInputError} />


                        </section>

                        <section className='bg-white gap-y-[10px] pb-[15px]' style={{ border: '1px solid #c3c4c7' }}>

                            <div className='p-[10px] m-0' style={{ borderBottom: '1px solid #c3c4c7' }}>
                                <span className='text-red-500 text-base'>*</span>
                                <span className='text-[16px] font-bold'>Ảnh hiển thị:</span>
                            </div>


                            <div className='flex justify-between p-[10px]'>
                                <a className='text-[13px]'>Chọn ảnh</a>

                                {
                                    mangaInput.featuredImage &&
                                    <img className='h-[120px] w-[120px]' src={mangaInput.featuredImage} />
                                }
                            </div>
                            {
                                !mangaInputError.featuredImage &&
                                <p className='text-[12px] text-red-500 px-[5px]'>
                                    {t(mangaInputError.featuredImage)}
                                </p>
                            }
                        </section>
                    </div>
                </div>
            }
        </div>
    );
}

export default AddEditMangaForm;
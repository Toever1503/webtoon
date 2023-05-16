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
    authors: string;
    genres: string;
    tags: string;
}

let isAutoSavingMangaInfo = false;
export async function autoSaveMangaInfo(mangaInput: MangaInput) {
    if (isAutoSavingMangaInfo) return;
    console.log('autoSaveMangaInfo: id ', mangaInput.id);

    isAutoSavingMangaInfo = true;
    try {
        const res = await mangaService.addMangaInfo(getMangaInputFormData(mangaInput));
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

const getMangaInputFormData = (mangaInput: MangaInput): FormData => {
    const formData = new FormData();
    if (mangaInput.id)
        formData.append('id', mangaInput.id.toString());

    formData.append('title', mangaInput.title);
    formData.append('description', mangaInput.description);
    formData.append('excerpt', mangaInput.excerpt);
    formData.append('mangaName', mangaInput.mangaName);
    formData.append('status', mangaInput.status);
    formData.append('mangaStatus', mangaInput.mangaStatus);
    formData.append('releaseYear', mangaInput.releaseYear?.toString() || `${new Date().getFullYear()}`);
    formData.append('mangaType', mangaInput.mangaType);
    formData.append('genres', mangaInput.genres.join(','));
    formData.append('authors', mangaInput.authors.join(','));
    formData.append('tags', mangaInput.tags.join(','));
    formData.append('isFree', mangaInput.isFree.toString());
    formData.append('isShow', mangaInput.isShow.toString());

    if (mangaInput.featuredImageFile)
        formData.append('featuredImageFile', mangaInput.featuredImageFile);
    else
        formData.append('featuredImage', mangaInput.featuredImage);
    formData.append('displayType', mangaInput.displayType);

    return formData;
}

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
        title: '',
        description: '',
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
        isShow: true,
        featuredImage: '',
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
        mangaInput.description = mangaContent;
        mangaInput.excerpt = mangaExcerpt;

        console.log('mangaInput', mangaInput);

        let errorCount = 0;
        if (!mangaInput.title) {
            errorCount++;
            mangaInputError.title = 'manga.form.errors.title-required';
        }
        else mangaInputError.title = '';

        if (mangaExcerpt.length == 0) {
            errorCount++;
            mangaInputError.description = 'manga.form.errors.content-required';
        }
        else mangaInputError.description = '';

        if (mangaInput.genres.length === 0) {
            errorCount++;
            mangaInputError.genres = 'manga.form.errors.genres-required';
        }
        else mangaInputError.genres = '';

        if (mangaInput.authors.length === 0) {
            errorCount++;
            mangaInputError.authors = 'manga.form.errors.authors-required';
        }
        else mangaInputError.authors = '';

        if (mangaInput.tags.length === 0) {
            errorCount++;
            mangaInputError.tags = 'manga.form.errors.tags-required';
        }
        else mangaInputError.tags = '';


        if (!mangaInput.featuredImage) {
            errorCount++;
            mangaInputError.featuredImage = 'manga.form.errors.featured-image-required';
        }
        else mangaInputError.featuredImage = '';
        setMangaInputError({ ...mangaInputError });

        if (errorCount === 0) {
            console.log('begin save manga');

            setIsSavingMangaInfo(true);
            mangaInput.status = 'PUBLISHED';


            const formData = getMangaInputFormData(mangaInput);


            mangaService.addMangaInfo(formData)
                .then((res) => {
                    console.log('add manga success', res.data);

                    if (props.type === 'ADD')
                        dispatch(showNofification({
                            type: 'success',
                            message: t('manga.form.errors.create-success'),
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
                            type: 'error',
                            message: t('manga.form.errors.create-failed'),
                        }));
                    else
                        dispatch(showNofification({
                            type: 'error',
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
        authors: '',
        genres: '',
        tags: '',
    });

    const onChooseFeaturedImage = () => {
        let inputTag = document.createElement('input');
        inputTag.type = 'file';
        inputTag.accept = 'image/*';
        inputTag.onchange = (e: any) => {
            let file: File = e.target.files[0];

            if (file.type.indexOf('image') === -1) {
                dispatch(showNofification({
                    type: 'error',
                    message: t('manga.form.errors.featured-image-invalid'),
                }));
                return;
            }

            mangaInput.featuredImageFile = file;
            // read base64 

            var reader = new FileReader();
            reader.onloadend = function () {
                console.log('RESULT', reader.result);
                setMangaInput({
                    ...mangaInput,
                    featuredImage: reader.result as string
                });
            }
            reader.readAsDataURL(file);
        };
        inputTag.click();
    }

    let { id } = useParams();
    const [isFetchingMangaInfo, setIsFetchingMangaInfo] = useState<boolean>(true);
    useEffect(() => {
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
                    <div className='w-[1400px]'>
                        <section className='border h-fit'>
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

                            <div className='w-full'>
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

                            <div className='flex justify-between items-center px-[10px]'>
                                <span className='text-[14px] font-bold'>Trạng thái:</span>
                                <Select
                                    className='min-w-[150px]'
                                    defaultValue={mangaInput.isShow}
                                    onChange={(val) => { 
                                        setMangaInput({ ...mangaInput, isShow: Boolean(val) });
                                     }}
                                    options={[
                                        { value: true, label: 'Đăng ngay' },
                                        { value: false, label: 'Ẩn' },
                                    ]}
                                />
                            </div>

                            <div className='flex justify-between items-center px-[10px]'>
                                <span className='text-[14px] font-bold'>Tình trạng phát hành:</span>
                                <Select
                                    className='min-w-[150px]'
                                    defaultValue="COMING"
                                    onChange={(val: ReleaseStatus) => { setMangaInput({ ...mangaInput, mangaStatus: val }) }}
                                    options={[
                                        { value: 'COMING', label: 'Sắp ra mắt' },
                                        { value: 'GOING', label: 'Đang ra' },
                                        { value: 'ON_STOPPING', label: 'Đang tạm dừng' },
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


                            <div className='grid p-[10px]'>
                                <a className='text-[13px] mb-[20px] block' onClick={onChooseFeaturedImage}>Chọn ảnh</a>

                                {
                                    mangaInput.featuredImage &&
                                    <img className='h-[250px] w-[200px]' src={mangaInput.featuredImage} />
                                }
                            </div>
                            {
                                mangaInputError.featuredImage.length > 0 &&
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
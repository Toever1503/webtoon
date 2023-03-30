import React, { ChangeEvent, ChangeEventHandler, EventHandler, useEffect, useRef, useState } from 'react';
import { Button, Select, Table, TablePaginationConfig, Tag, Input, DatePicker, Divider, Space, InputRef } from 'antd';
import MangaChapterForm from './component/MangaChapterForm';
import { PlusOutlined } from '@ant-design/icons';
import debounce from '../../utils/debounce';
import RichtextEditorForm from '../../components/RichtextEditorForm';
import { RichTextEditorComponent } from '@syncfusion/ej2-react-richtexteditor';
import { useTranslation } from "react-i18next";
import dayjs from 'dayjs';
import mangaService, { MangaInput, MangaStatus, ReleaseStatus } from '../../services/manga/MangaService';
import { useDispatch } from 'react-redux';
import { showNofification } from '../../stores/features/notification/notificationSlice';
import { NavigateFunction, useNavigate } from 'react-router-dom';
import { AppDispatch } from '../../stores';



interface MangaInputError {
    title: string;
    description: string;
    genres: string;
    authors: string;
    featureImage: string;
    tags: string;
}

let isAutoSavingMangaInfo = false;
export function autoSaveMangaInfo(mangaInput: MangaInput) {
    if (isAutoSavingMangaInfo) return;
    console.log('autoSaveMangaInfo: id ', mangaInput.id);

    isAutoSavingMangaInfo = true;
    mangaService.addMangaInfo(mangaInput)
        .then((res) => {
            console.log('auto save manga success', res.data);
            mangaInput.id = res.data.id;
        })
        .catch((err) => {
            console.log('auto save manga failed');
            console.log(err);
        })
        .finally(() => {
            isAutoSavingMangaInfo = false;
        })
};

const AddNewManga: React.FC = () => {
    const { t, i18n } = useTranslation();
    const dispatch: AppDispatch = useDispatch();
    const navigate: NavigateFunction = useNavigate();
    // begin tag search
    const [genreOptions, setGenreOptions] = useState(['jack', 'lucy']);
    const [genreSearchVal, setGenreSearchVal] = useState('');
    const genreSearchRef = useRef<InputRef>(null);
    const [isAddingNewGenre, setIsAddingNewGenre] = useState<boolean>(false);
    const onSearchGenre = debounce((val: string) => {
        setTagSearchVal(val);
        console.log('search tag: ', val);
    });

    const addGenre = (e: React.MouseEvent<HTMLButtonElement | HTMLAnchorElement>) => {
        console.log('add tag:', e.target);
    }
    // end tag search

    // begin author search
    const [authorOptions, setAuthorOptions] = useState(['jack', 'lucy']);
    const [authorSearchVal, setAuthorSearchVal] = useState('');
    const authorSearchRef = useRef<InputRef>(null);
    const [isAddingNewAuthor, setIsAddingNewAuthor] = useState<boolean>(false);
    const onSearchAuthor = debounce((val: string) => {
        setAuthorSearchVal(val);
        console.log('search tag: ', val);
    });

    const addAuthor = (e: React.MouseEvent<HTMLButtonElement | HTMLAnchorElement>) => {
        console.log('add tag:', e.target);
    }
    // end author search

    // begin tag search
    const [tagOptions, setTagOptions] = useState(['jack', 'lucy']);
    const [tagSearchVal, setTagSearchVal] = useState('');
    const tagSearchRef = useRef<InputRef>(null);
    const [isAddingNewTag, setIsAddingNewTag] = useState<boolean>(false);
    const onSearchTag = debounce((val: string) => {
        setTagSearchVal(val);
        console.log('search tag: ', val);
    });

    const addTag = (e: React.MouseEvent<HTMLButtonElement | HTMLAnchorElement>) => {
        console.log('add tag:', e.target);
    }
    // end tag search



    // manga info
    const [mangaInput, setMangaInput] = useState<MangaInput>({
        id: '',
        title: 'test',
        description: 'The Rich Text Editor component is WYSIWYG ("what you see is what you get") editor that provides the best user experience to create and update the content. Users can format their content using standard toolbar commands.',
        excerpt: '',
        mangaName: '',
        status: 'DRAFTED',
        mangaStatus: 'COMING',
        releaseYear: dayjs(),
        mangaType: 'UNSET',
        genres: ['lucy'],
        authors: ['lucy'],
        tags: ['lucy'],
        featureImage: 'http://ima.ac',
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

        setIsSavingMangaInfo(true);

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

        // if (!mangaInput.genres || mangaInput.genres.length === 0) {
        //     errorCount++;
        //     mangaInputError.genres = 'manga.form.errors.genres-required';
        // }

        // if (!mangaInput.authors || mangaInput.authors.length === 0) {
        //     errorCount++;
        //     mangaInputError.authors = 'manga.form.errors.authors-required';
        // }

        if (!mangaInput.featureImage) {
            errorCount++;
            mangaInputError.featureImage = 'manga.form.errors.feature-image-required';
        }
        else mangaInputError.featureImage = '';
        setMangaInputError({ ...mangaInputError });

        if (errorCount === 0) {
            console.log('begin save manga');

            setIsSavingMangaInfo(true);
            mangaService.addMangaInfo(mangaInput)
                .then((res) => {
                    console.log('add manga success', res.data);
                    dispatch(showNofification({
                        type: 'success',
                        message: t('manga.form.errors.add-success'),
                    }));
                    navigate(`/manga`);
                })
                .catch((err) => {
                    console.log(err);
                    dispatch(showNofification({
                        type: 'error',
                        message: t('manga.form.errors.add-failed'),
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
        genres: '',
        authors: '',
        featureImage: '',
        tags: '',
    });


    useEffect(() => {
        let id: number | undefined;
        id = setInterval(() => {
            autoSaveMangaInfo(mangaInput);
        }, 15000);
        return () => clearInterval(id);
    }, [mangaInput]);


    return (
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
                <p className="text-[23px] font-[600]">Add New Manga</p>
                <Button type="primary" loading={isSavingMangaInfo} onClick={onSaveMangaInfo}>Save</Button>
            </div>

            <div className='flex gap-[15px]'>
                <div className=''>
                    <section className='border max-w-[1000px] h-fit'>
                        <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                            <span className='text-red-500'>*</span>
                            <span> Title:</span>
                        </label>
                        <Input className='' value={mangaInput.title} onChange={(val: any) => setMangaInput({ ...mangaInput, title: val.target.value })} placeholder="Title" />
                        {
                            <p className='text-[12px] text-red-500 px-[5px]'>
                                {mangaInputError.title && t(mangaInputError.title)}
                            </p>
                        }

                        <div className=''>
                            <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                                <span className='text-red-500'>*</span>
                                <span>Content:</span>
                            </label>
                            <RichtextEditorForm onReady={onReadyMangaContentEditor} />
                            {
                                <p className='text-[12px] text-red-500 px-[5px]'>
                                    {mangaInputError.description && t(mangaInputError.description)}
                                </p>
                            }
                        </div>
                    </section>

                    <MangaChapterForm mangaInput={mangaInput} setMangaInput={setMangaInput} />
                </div>

                <div className='manga-form-sidebar w-[280px] grid gap-y-[15px]' >
                    <section className='bg-white grid gap-y-[10px] pb-[15px]' style={{ border: '1px solid #c3c4c7' }}>
                        <p className='text-[18px] font-bold py-[10px] px-[10px] m-0' style={{ borderBottom: '1px solid #c3c4c7' }}>More Info</p>

                        <div className='grid gap-y-[5px] px-[10px] mt-[10px]'>
                            <span className='text-[14px] font-bold'>Alternative Title:</span>
                            <Input value={mangaInput.alternativeTitle} onChange={(e: ChangeEvent<HTMLInputElement>) => setMangaInput({ ...mangaInput, alternativeTitle: e.target.value })} placeholder='alternate title' />
                        </div>


                        <div className='flex justify-between items-center px-[10px]'>
                            <span className='text-[14px] font-bold'>Manga Status:</span>
                            <Select
                                className='min-w-[150px]'
                                value={mangaInput.status}
                                onChange={(val: MangaStatus) => { setMangaInput({ ...mangaInput, status: val }) }}
                                options={[
                                    { value: 'PUBLISHED', label: 'PUBLISHED' },
                                    { value: 'DRAFTED', label: 'DRAFTED' },
                                ]}
                            />
                        </div>

                        <div className='flex justify-between items-center px-[10px]'>
                            <span className='text-[14px] font-bold'>Release status:</span>
                            <Select
                                className='min-w-[150px]'
                                defaultValue="COMING"
                                onChange={(val: ReleaseStatus) => { setMangaInput({ ...mangaInput, mangaStatus: val }) }}
                                options={[
                                    { value: 'COMING', label: 'COMING' },
                                    { value: 'GOING', label: 'GOING' },
                                    { value: 'STOPPED', label: 'STOPED' },
                                    { value: 'CANCELLED', label: 'CANCELED' },
                                    { value: 'COMPLETED', label: 'COMPLETED' },
                                ]}
                            />
                        </div>

                        <div className='flex justify-between items-center px-[10px]'>
                            <span className='text-[14px] font-bold'>Release year:</span>
                            <DatePicker allowClear={false} mode='year' onChange={(val: any) => setMangaInput({ ...mangaInput, releaseYear: val })} value={mangaInput.releaseYear} placement='bottomRight' picker="year" />
                        </div>

                        <div className='grid gap-y-[5px] px-[10px]'>
                            <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                                <span className='text-red-500'>*</span>
                                <span> Genres:</span>
                            </label>
                            <Select
                                className='w-full'
                                mode="multiple"
                                placeholder="custom dropdown render"
                                // @ts-ignore
                                onSearch={onSearchGenre}
                                labelInValue
                                showSearch
                                allowClear
                                filterOption={false}
                                dropdownRender={(menu) => (
                                    <>
                                        {menu}
                                        <Divider style={{ margin: '8px 0' }} />
                                        <Space style={{ padding: '0 8px 4px' }}>
                                            <Input
                                                placeholder="Please enter item"
                                                ref={genreSearchRef}
                                            />
                                            <Button type="text" icon={<PlusOutlined />} loading={isAddingNewGenre} onClick={addTag}>
                                                Add item
                                            </Button>
                                        </Space>
                                    </>
                                )}
                                value={mangaInput.genres}
                                onChange={(val: any) => { setMangaInput({ ...mangaInput, genres: val.map((e: any) => e.value) }) }}
                                options={genreOptions.map((item) => ({ label: item, value: item }))}
                            />

                            {
                                mangaInputError.genres &&
                                <p className='text-[12px] text-red-500 px-[5px]'>
                                    {t(mangaInputError.genres)}
                                </p>
                            }
                        </div>

                        <div className='grid gap-y-[5px] px-[10px]'>
                            <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                                <span className='text-red-500'>*</span>
                                <span> Authors:</span>
                            </label>
                            <Select
                                className='w-full'
                                placeholder="custom dropdown render"
                                mode="multiple"
                                // @ts-ignore
                                onSearch={onSearchAuthor}
                                labelInValue
                                showSearch
                                allowClear
                                filterOption={false}
                                value={mangaInput.authors}
                                onChange={(val: any) => { setMangaInput({ ...mangaInput, authors: val.map((e: any) => e.value) }) }}
                                dropdownRender={(menu) => (
                                    <>
                                        {menu}
                                        <Divider style={{ margin: '8px 0' }} />
                                        <Space style={{ padding: '0 8px 4px' }}>
                                            <Input
                                                placeholder="Please enter item"
                                                ref={authorSearchRef}
                                            />
                                            <Button type="text" icon={<PlusOutlined />} loading={isAddingNewAuthor} onClick={addAuthor}>
                                                Add item
                                            </Button>
                                        </Space>
                                    </>
                                )}
                                options={authorOptions.map((item) => ({ label: item, value: item }))}
                            />

                            {
                                mangaInputError.authors &&
                                <p className='text-[12px] text-red-500 px-[5px]'>
                                    {t(mangaInputError.authors)}
                                </p>
                            }
                        </div>

                        <div className='grid gap-y-[5px] px-[10px]'>
                            <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                                <span className='text-red-500'>*</span>
                                <span> Tags:</span>
                            </label>
                            <Select
                                className='w-full'
                                mode="multiple"
                                placeholder="choose tag"
                                // @ts-ignore
                                onSearch={onSearchTag}
                                labelInValue
                                showSearch
                                allowClear
                                filterOption={false}
                                value={mangaInput.tags}
                                onChange={(val: any) => { setMangaInput({ ...mangaInput, tags: val.map((e: any) => e.value) }) }}
                                dropdownRender={(menu) => (
                                    <>
                                        {menu}
                                        <Divider style={{ margin: '8px 0' }} />
                                        <Space style={{ padding: '0 8px 4px' }}>
                                            <Input
                                                placeholder="Please enter item"
                                                ref={tagSearchRef}
                                            />
                                            <Button type="text" icon={<PlusOutlined />} loading={isAddingNewTag} onClick={addTag}>
                                                Add item
                                            </Button>
                                        </Space>
                                    </>
                                )}
                                options={tagOptions.map((item) => ({ label: item, value: item }))}
                            />

                            {
                                mangaInputError.tags &&
                                <p className='text-[12px] text-red-500 px-[5px]'>
                                    {t(mangaInputError.tags)}
                                </p>
                            }
                        </div>

                    </section>

                    <section className='bg-white gap-y-[10px] pb-[15px]' style={{ border: '1px solid #c3c4c7' }}>

                        <div className='p-[10px] m-0' style={{ borderBottom: '1px solid #c3c4c7' }}>
                            <span className='text-red-500 text-base'>*</span>
                            <span className='text-[18px] font-bold'> Featured image:</span>
                        </div>


                        <div className='flex justify-between p-[10px]'>
                            <a className='text-[13px]'>Choose image</a>

                            {
                                mangaInput.featureImage &&
                                <img className='h-[120px] w-[120px]' src='https://s.pstatic.net/static/newsstand/2023/0322/article_img/new_main/9044/121821_001.jpg' />
                            }
                        </div>
                        {
                            !mangaInputError.featureImage &&
                            <p className='text-[12px] text-red-500 px-[5px]'>
                                {t(mangaInputError.featureImage)}
                            </p>
                        }
                    </section>
                </div>
            </div>
        </div>
    );
}

export default AddNewManga;
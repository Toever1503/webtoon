import React, { useRef, useState } from 'react';
import { Button, Select, Table, TablePaginationConfig, Tag, Input, DatePicker, Divider, Space, InputRef } from 'antd';
import MangaChapterForm from './component/MangaChapterForm';
import { PlusOutlined } from '@ant-design/icons';
import debounce from '../../utils/debounce';
import RichtextEditorForm from '../../components/RichtextEditorForm';
import { RichTextEditorComponent } from '@syncfusion/ej2-react-richtexteditor';

const AddNewManga: React.FC = () => {
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

    const [mangaContentEditorRef, setMangaContentEditorRef] = useState<RichTextEditorComponent>();
    const onReadyMangaContentEditor = (rteObj: RichTextEditorComponent) => {
        setMangaContentEditorRef(rteObj);
    };

    return (
        <div className="space-y-3 py-3">
            <div className="flex space-x-3">
                <p className="text-[23px] font-[600]">Add New Manga</p>
            </div>

            <div className='flex gap-[15px] justify-between'>
                <div className='w-[calc(100%_-_280px)] grid gap-y-[15px]'>
                    <section className='border'>
                        <Input placeholder="Title" />

                        <div className='max-w-[700px]'>
                            <RichtextEditorForm onReady={onReadyMangaContentEditor} />
                        </div>
                    </section>

                    <MangaChapterForm />
                </div>

                <div className='manga-form-sidebar w-[280px] grid gap-y-[15px]' >
                    <section className='bg-white grid gap-y-[10px] pb-[15px]' style={{ border: '1px solid #c3c4c7' }}>
                        <p className='text-[18px] font-bold py-[10px] px-[10px] m-0' style={{ borderBottom: '1px solid #c3c4c7' }}>More Info</p>
                        <div className='flex justify-between items-center px-[10px]'>
                            <span className='text-[14px] font-bold'>Status:</span>
                            <Select
                                className='w-fit'
                                defaultValue="lucy"
                                options={[
                                    { value: 'jack', label: 'PUBLISHED' },
                                    { value: 'lucy', label: 'DELETED' },
                                    { value: 'Yiminghe', label: 'DRAFTED' },
                                ]}
                            />
                        </div>

                        <div className='flex justify-between items-center px-[10px]'>
                            <span className='text-[14px] font-bold'>Posted on:</span>
                            <DatePicker placement='bottomRight' />
                        </div>

                        <div className='flex justify-between items-center px-[10px]'>
                            <span className='text-[14px] font-bold'>Release year:</span>
                            <DatePicker placement='bottomRight' picker="year" />
                        </div>

                        <div className='grid gap-y-[10px] px-[10px]'>
                            <span className='text-[14px] font-bold'>Genres:</span>

                            <Select
                                className='w-full'
                                mode="multiple"
                                placeholder="custom dropdown render"
                                // @ts-ignore
                                onSearch={onSearchTag}
                                labelInValue
                                showSearch
                                filterOption={false}
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
                        </div>

                        <div className='grid gap-y-[10px] px-[10px]'>
                            <span className='text-[14px] font-bold'>Authors:</span>

                            <Select
                                className='w-full'
                                placeholder="custom dropdown render"
                                mode="multiple"
                                // @ts-ignore
                                onSearch={onSearchAuthor}
                                labelInValue
                                showSearch
                                filterOption={false}
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
                        </div>

                    </section>

                    <section className='bg-white grid gap-y-[10px] pb-[15px]' style={{ border: '1px solid #c3c4c7' }}>
                        <p className='text-[18px] font-bold p-[10px] m-0' style={{ borderBottom: '1px solid #c3c4c7' }}>Featured image</p>
                        <div className='flex justify-between px-[10px]'>
                            <a className='text-[13px]'>Choose image</a>

                            <img className='h-[120px] w-[120px]' src='https://s.pstatic.net/static/newsstand/2023/0322/article_img/new_main/9044/121821_001.jpg' />
                        </div>
                    </section>
                </div>
            </div>
        </div>
    );
}

export default AddNewManga;
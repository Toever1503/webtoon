import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { RichTextEditorComponent } from "@syncfusion/ej2-react-richtexteditor";
import { Button, Checkbox, Divider, Input, InputRef, message, Radio, Select, Space } from "antd";
import { CheckboxChangeEvent } from "antd/es/checkbox";
import { AxiosResponse } from "axios";
import { useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import RichtextEditorForm from "../../../../components/RichtextEditorForm";
import mangaService, { MangaInput } from "../../../../services/MangaService";
import { showNofification } from "../../../../stores/features/notification/notificationSlice";

type MangaUploadSingleChapterProps = {
    mangaInput: MangaInput,
}

interface ChapterInput {
    volume: number,
    chapterName: string,
    chapterIndex?: number,
    chapterContent: string,
}

interface ChapterInputError {
    chapterName: string,
    chapterContent: string,
    chapterImages: string,
}

export type MangaVolumeOptionType = {
    id: number,
    name: string,
    volumeIndex?: number,
}

const MangaUploadSingleChapter: React.FC<MangaUploadSingleChapterProps> = (props: MangaUploadSingleChapterProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();
    // begin volume select
    const [volumeOptions, setVolumeOptions] = useState<MangaVolumeOptionType[]>([
        {
            id: 0,
            name: 'Volume 1'
        }
    ]);

    const [isAddingNewVolume, setIsAddingNewVolume] = useState<boolean>(false);
    const [volumeVal, setVolumeVal] = useState<string>('');
    const addNewVolume = () => {
        console.log('add tag:', volumeVal);
        if (volumeVal) {
            setIsAddingNewVolume(true);

            mangaService.createNewVolume(
                {
                    mangaId: props.mangaInput.id,
                    name: volumeVal,
                    volumeIndex: 0
                }
            )
                .then((res) => {
                    console.log(res.data);
                    setVolumeOptions([...volumeOptions, {
                        id: res.data.id,
                        name: volumeVal
                    }]);
                    message.success(t('manga.form.success.create-volume'));
                    setVolumeVal('');
                })
                .catch((err) => {
                    console.log(err);
                    message.error(t('manga.form.errors.create-volume-failed'));
                })
                .finally(() => {
                    setIsAddingNewVolume(false);
                });
        }
        else {
            message.error(t('manga.form.errors.volume-required'));
        }

    };
    // end volume select

    const [selectedVolume, setSelectedVolume] = useState<number>(volumeOptions[0].id);
    const [isRequireVipChapter, setIsRequireVipChapter] = useState<boolean>(false);

    const [chapterContentEditorRef, setChapterContentEditorRef] = useState<RichTextEditorComponent>();
    const onReadyMangaContentEditor = (rteObj: RichTextEditorComponent, setContent: Function) => {
        setChapterContentEditorRef(rteObj);
        setMangaTextChapter = setContent;
    };
    let setMangaTextChapter: Function = () => { };

    const uploadTextChapterByFile = () => {
        const fileInput = document.createElement('input');
        fileInput.type = 'file';
        fileInput.accept = '.txt,.doc,.docx';
        fileInput.onchange = (e) => {
            const file = fileInput.files?.[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    const content = reader.result;
                    if (content) {
                        setMangaTextChapter(content.toString());
                    }
                };
                reader.readAsText(file);
            }
        };
        fileInput.click();
    }


    // begin image chapter
    const [imageUploadType, setImageUploadType] = useState<'ZIP' | 'IMAGE'>('ZIP');
    const [imageChapterFiles, setImageChapterFiles] = useState<File[]>([]);
    const uploadImageChapter = () => {
        const fileInput = document.createElement('input');
        fileInput.type = 'file';
        // fileInput.accept = '.png,.jpg,.jpeg,.zip';
        fileInput.accept = '.zip';
        fileInput.multiple = true;
        fileInput.onchange = () => {
            const fileList: FileList | null = fileInput.files;
            const files: File[] = [];
            if (fileList) {
                for (let i = 0; i < fileList.length; i++) {
                    files.push(fileList[i]);
                }
                setImageChapterFiles(files)
                console.log('files', files);

            }
        };
        fileInput.click();
    };
    const deleteImageChapterFile = (index: number) => {
        const newFiles = imageChapterFiles?.filter((file: File, i) => i !== index);
        setImageChapterFiles(newFiles);
    }

    // begin chapter input
    const [chapterInput, setChapterInput] = useState<ChapterInput>({
        volume: 0,
        chapterName: '',
        chapterIndex: 0,
        chapterContent: '',
    });

    const [chapterInputError, setChapterInputError] = useState<ChapterInputError>({
        chapterName: '',
        chapterContent: '',
        chapterImages: '',
    });

    const resetChapterInput = () => {
        setChapterInput({
            volume: 0,
            chapterName: '',
            chapterIndex: 0,
            chapterContent: '',
        });
        setChapterInputError({
            chapterName: '',
            chapterContent: '',
            chapterImages: '',
        });
        setMangaTextChapter('');
        setImageChapterFiles([]);
    }

    const [isCreatingChapter, setIsCreatingChapter] = useState<boolean>(false);
    const onCreateChapter = () => {
        console.log('on create chapter', imageChapterFiles);

        let errorCount = 0;
        if (!chapterInput.chapterName) {
            chapterInputError.chapterName = 'manga.form.errors.chapter-name-required';
            errorCount++;
        }
        else chapterInputError.chapterName = '';

        if (props.mangaInput.mangaType === 'IMAGE') {
            if (imageChapterFiles.length === 0) {
                chapterInputError.chapterImages = 'manga.form.errors.chapter-images-required';
                errorCount++;
            }
            else chapterInputError.chapterImages = '';
        }
        else if (props.mangaInput.mangaType === 'TEXT') {
            const chapterContent = chapterContentEditorRef?.getHtml() || '';
            setChapterInput({
                ...chapterInput,
                chapterContent: chapterContent
            });
            if (!chapterContent) {
                chapterInputError.chapterContent = 'manga.form.errors.chapter-content-required';
                errorCount++;
            }
            else chapterInputError.chapterContent = '';
        }
        setChapterInputError({ ...chapterInputError });

        if (errorCount === 0) {
            setIsCreatingChapter(true);
            if (props.mangaInput.mangaType === 'IMAGE') {
                console.log('create image chapter');

                const formdata = new FormData();
                formdata.append('chapterIndex', chapterInput.chapterIndex ? chapterInput.chapterIndex.toString() : '0');
                formdata.append('chapterName', chapterInput.chapterName);
                formdata.append('isRequiredVip', isRequireVipChapter.toString());
                formdata.append('volumeID', chapterInput.volume.toString());
                formdata.append('mangaID', props.mangaInput.id.toString());
                imageChapterFiles.forEach((file: File) => {
                    formdata.append('multipartFiles', file);
                });
                mangaService
                    .createImageChapter(formdata)
                    .then((res) => {
                        console.log('create image chapter success:', res.data);
                        dispatch(showNofification({
                            type: 'success',
                            message: t('manga.form.success.create-chapter')
                        }));
                        resetChapterInput();
                    })
                    .catch((err) => {
                        console.log('create image chapter error:', err);
                        dispatch(showNofification({
                            type: 'error',
                            message: t('manga.form.errors.create-chapter')
                        }));
                    })
                    .finally(() => {
                        setIsCreatingChapter(false);
                    });

            }
            else if (props.mangaInput.mangaType === 'TEXT') {
                mangaService.createTextChapter({
                    chapterIndex: chapterInput.chapterIndex ? chapterInput.chapterIndex : 0,
                    chapterName: chapterInput.chapterName,
                    chapterContent: chapterContentEditorRef?.getHtml(),
                    isRequiredVip: isRequireVipChapter,
                    volumeID: chapterInput.volume,
                    mangaID: props.mangaInput.id
                })
                    .then((res) => {
                        console.log('create text chapter success:', res.data);
                        dispatch(showNofification({
                            type: 'success',
                            message: t('manga.form.success.create-chapter')
                        }));
                        resetChapterInput();
                    })
                    .catch((err) => {
                        console.log('create text chapter error:', err);
                        dispatch(showNofification({
                            type: 'error',
                            message: t('manga.form.errors.create-chapter-failed')
                        }))
                    })
                    .finally(() => {
                        setIsCreatingChapter(false);
                    })
            }
        }
        else {
            console.log(chapterInputError);

            dispatch(showNofification({
                type: 'error',
                message: t('manga.form.errors.check-again')
            }))
        }
    }

    useEffect(() => {
        mangaService
            .getVolumeForManga(props.mangaInput.id)
            .then((res: AxiosResponse<MangaVolumeOptionType[]>) => {
                setVolumeOptions(res.data);
            })
            .catch((err) => {
                console.log('get volume error:', err);
            })
    }, [props]);

    return (
        <div className="px-[15px] grid gap-y-[10px]">
            <section>
                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                    <span> Volume:</span>
                </label>
                <Select
                    className="min-w-[250px]"
                    placeholder="custom dropdown render"
                    // @ts-ignore
                    labelInValue
                    showSearch
                    value={selectedVolume}
                    filterOption={true}
                    onChange={(value) => setSelectedVolume(value)}
                    dropdownRender={(menu) => (
                        <>
                            {menu}
                            <Divider style={{ margin: '8px 0' }} />
                            <Space style={{ padding: '0 8px 4px' }}>
                                <Input
                                    placeholder="Please enter item"
                                    value={volumeVal}
                                    onChange={(e) => setVolumeVal(e.target.value)}
                                />
                                <Button type="text" icon={<PlusOutlined />} loading={isAddingNewVolume} onClick={addNewVolume}>
                                    Add item
                                </Button>
                            </Space>
                        </>
                    )}
                    options={volumeOptions.map((item) => ({ label: item.name, value: item.id }))}
                />
            </section>

            <section>

                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px]'>
                    <span className='text-red-500'>*</span>
                    <span> Chapter name:</span>
                </label>
                <Input placeholder="enter your chapter name" value={chapterInput.chapterName} onChange={val => setChapterInput({ ...chapterInput, chapterName: val.target.value })} />
                {
                    <p className='text-[12px] text-red-500 px-[5px] m-0'>
                        {chapterInputError.chapterName && t(chapterInputError.chapterName)}
                    </p>
                }
            </section>

            <section>
                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                    <span> Chapter Index(Optional):</span>
                </label>
                <Input type="number" placeholder="enter index" value={chapterInput.chapterIndex} onChange={val => setChapterInput({ ...chapterInput, chapterIndex: Number(val.target.value) })} />
            </section>
            <section className="flex items-center space-x-3">
                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                    <span> Require vip:</span>
                </label>
                <Checkbox className="mt-[5px]" checked={isRequireVipChapter} onChange={(val: CheckboxChangeEvent) => setIsRequireVipChapter(val.target.checked)} />
            </section>

            {/* for text chapter content */}
            {
                props.mangaInput.mangaType === 'TEXT' &&
                <section >
                    <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px]'>
                        <span className='text-red-500'>*</span>
                        <span> Chapter content:</span>
                    </label>

                    <a className="mb-[10px] block text-[12px]" onClick={uploadTextChapterByFile}>Upload text file (Support .txt, .docs)</a>

                    <div className='max-w-[700px]'>
                        <RichtextEditorForm onReady={onReadyMangaContentEditor} />
                    </div>

                    {
                        <p className='text-[12px] text-red-500 px-[5px]  m-0'>
                            {chapterInputError.chapterContent && t(chapterInputError.chapterContent)}
                        </p>
                    }
                </section>

            }

            {/* for image chapter content */}

            {
                props.mangaInput.mangaType === 'IMAGE' &&
                <section >
                    <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px]'>
                        <span className='text-red-500'>*</span>
                        <span> Chapter Image:</span>
                    </label>

                    {/* <Radio.Group className="py-[5px]" size="small" value={imageUploadType} onChange={e => setImageUploadType(e.target.value)} >
                            <Radio value={'ZIP'} className="text-[12px]">zip file</Radio>
                            <Radio value={'IMAGE'} className="text-[12px]">Images</Radio>
                        </Radio.Group> */}

                    <a className="mb-[10px] block text-[12px] underline" onClick={uploadImageChapter}>Upload .zip folder(maximum 100MB), all files aren't format will be discard</a>

                    <div>
                        {imageChapterFiles && imageChapterFiles.length > 0 && (
                            <ul className="list-disc list-inside">
                                {Array.from(imageChapterFiles).map((file, index) => (
                                    <li key={index}>
                                        <span>{file.name}</span>
                                        <DeleteOutlined onClick={() => deleteImageChapterFile(index)} className="px-[5px] hover:text-red-500 cursor-pointer" />
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>
                    {
                        <p className='text-[12px] text-red-500 px-[5px]  m-0'>
                            {chapterInputError.chapterImages && t(chapterInputError.chapterImages)}
                        </p>
                    }
                </section>
            }



            <Button type="primary" className="w-fit" onClick={onCreateChapter} loading={isCreatingChapter}>
                Create chapter
            </Button>
        </div>)
};


export default MangaUploadSingleChapter;
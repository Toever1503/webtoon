import { DeleteOutlined } from "@ant-design/icons";
import { RichTextEditorComponent } from "@syncfusion/ej2-react-richtexteditor";
import { Button, Checkbox, Input, Modal } from "antd";
import { CheckboxChangeEvent } from "antd/es/checkbox";
import { AxiosResponse } from "axios";
import React, { Suspense, useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import Sortable from "sortablejs";
import RichtextEditorForm from "../../../../components/RichtextEditorForm";
import mangaService, { MangaInput } from "../../../../services/manga/MangaService";
import { showNofification } from "../../../../stores/features/notification/notificationSlice";
import { ChapterType } from "./MangaVolumeInfoItem";

type MangaUploadChapterModalProps = {
    mangaInput: MangaInput,
    visible: boolean,
    onOk: (chapterInput: ChapterType) => void,
    onCancel: Function,
    title: string,
    chapterInput?: ChapterType,
    volumeId: string | number,
}

interface ChapterForm {
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
type ImageChapterFileType = {
    index: number,
    file: File | string,
    data: string,
    url?: string,
}


type CreateImageHtmlElementProps = {
    file: ImageChapterFileType,
    index: number,
    removeImage: Function,
}

const CreateImageHtmlElement: React.FC<CreateImageHtmlElementProps> = ({ file, index, removeImage }: CreateImageHtmlElementProps) => {
    const fileRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        console.log('fileRef: ',);
        // @ts-ignore
        fileRef.current.chapterFile = file;
    }, [])
    return <>
        <div ref={fileRef} data-id={file.index} className="w-[102px] h-[120px] relative" style={{ border: '1px solid #e3e3e3' }} key={index}>
            <img className="h-full w-full cursor-pointer" src={file.file ? file.data : file.url} />
            <div className="absolute top-0 right-0 bg-slate-200 p-[3px] rounded">
                <DeleteOutlined onClick={() => removeImage(index)} className="hover:text-red-500 cursor-pointer" />
            </div>
        </div>
    </>
}

const MangaUploadChapterModal: React.FC<MangaUploadChapterModalProps> = (props: MangaUploadChapterModalProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();
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
        fileInput.accept = '.txt';
        fileInput.onchange = (e) => {
            const file = fileInput.files?.[0];
            if (file) {
                console.log('file', file.type);

                const allowType = ['text/plain', 'application/haansoftdocx', 'application/haansoftdoc'];
                if (file.size > 100000000) {
                    dispatch(showNofification({
                        type: 'error',
                        message: t('manga.form.chapter.errors.file-size-exceed'),
                    }));
                    return;
                }
                // @ts-ignore
                else if (!allowType.includes(file.type)) {

                    dispatch(showNofification({
                        type: 'error',
                        message: t('manga.form.chapter.errors.only-txt-allowed'),
                    }));
                    return;
                }

                const reader = new FileReader();
                reader.onload = (e) => {
                    if ((reader.result?.toString().length || 0) > 30000) {
                        dispatch(showNofification({
                            type: 'error',
                            message: t('manga.form.chapter.errors.exceed-character'),
                        }));
                        return;
                    }
                    const content = reader.result;
                    console.log('content', reader);
                    if (content) {
                        setMangaTextChapter(content.toString());
                    }
                };
                if (file.type === 'text/plain')
                    reader.readAsText(file);
                else reader.readAsBinaryString(file);

            }
        };
        fileInput.click();
    }


    // begin image chapter

    const [imageChapterFiles, setImageChapterFiles] = useState<ImageChapterFileType[]>([]);
    const uploadImageChapter = () => {
        if (imageChapterFiles.length >= 30) {
            dispatch(showNofification({
                type: 'error',
                message: t('manga.form.errors.exceed-image-chapter'),
            }));
            return;
        }

        const fileInput = document.createElement('input');
        fileInput.type = 'file';
        fileInput.accept = 'image/*';
        fileInput.multiple = true;
        fileInput.onchange = () => {
            const fileList: FileList | null = fileInput.files;
            const files: ImageChapterFileType[] = [];
            if (fileList) {
                if ([...fileList].find((file: File) => !file.type.match('image.*'))) {
                    dispatch(showNofification({
                        type: 'error',
                        message: t('manga.form.errors.only-image-allowed'),
                    }));
                    return;
                }

                let lastIndex: number = imageChapterFiles.length;
                for (let i = 0; i < fileList.length; i++) {
                    files.push({
                        index: lastIndex++,
                        file: fileList[i],
                        data: URL.createObjectURL(fileList[i]),
                    });
                }
                setImageChapterFiles([...files, ...imageChapterFiles]);
                console.log('files', files);

            }
        };
        fileInput.click();
    };

    const deleteImageChapterFile = (index: number) => {
        const newFiles = imageChapterFiles?.filter((file: ImageChapterFileType, i) => i !== index);
        setImageChapterFiles(newFiles);
    }

    // begin chapter input
    const [chapterInput, setChapterInput] = useState<ChapterType>({
        id: '',
        name: '',
        chapterIndex: 0,
        content: '',
        chapterImages: [],
        isRequiredVip: false,
        volumeId: props.volumeId,
    });

    const [chapterInputError, setChapterInputError] = useState<ChapterInputError>({
        chapterName: '',
        chapterContent: '',
        chapterImages: '',
    });

    const resetChapterInput = () => {
        setChapterInput({
            id: '',
            name: '',
            chapterIndex: 0,
            content: '',
            chapterImages: [],
            isRequiredVip: false,
            volumeId: props.volumeId,
        });
        setChapterInputError({
            chapterName: '',
            chapterContent: '',
            chapterImages: '',
        });
        setIsRequireVipChapter(false);
        setMangaTextChapter('');
        setImageChapterFiles([]);
    }

    const [isCreatingChapter, setIsCreatingChapter] = useState<boolean>(false);
    const onCreateChapter = () => {
        console.log('on create chapter', imageChapterFiles);

        let errorCount = 0;
        if (!chapterInput.name) {
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
                content: chapterContent
            });
            if (!chapterContent) {
                chapterInputError.chapterContent = 'manga.form.errors.chapter-content-required';
                errorCount++;
            }
            else chapterInputError.chapterContent = '';
        }
        setChapterInputError({ ...chapterInputError });

        if (errorCount === 0) {
            console.log('adding chapter: ', chapterInput.volumeId);
            setIsCreatingChapter(true);
            if (props.mangaInput.mangaType === 'IMAGE') {
                console.log('create image chapter');

                const formdata = new FormData();
                formdata.append('chapterIndex', chapterInput?.chapterIndex?.toString() || '0');
                formdata.append('chapterName', chapterInput.name);
                formdata.append('isRequiredVip', isRequireVipChapter.toString());
                formdata.append('volumeId', chapterInput?.volumeId?.toString() || '0');
                document.querySelectorAll('#chapter-images-list > div').forEach((item) => {
                    // @ts-ignore
                    formdata.append('files', item.chapterFile.file);
                });

                mangaService
                    .createImageChapter(formdata, props.mangaInput.id)
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
                    name: chapterInput.name,
                    content: chapterInput.content,
                    isRequiredVip: isRequireVipChapter,
                    volumeId: chapterInput.volumeId || 0,
                }, props.mangaInput.id)
                    .then((res: AxiosResponse<ChapterType>) => {
                        console.log('create text chapter success:', res.data);
                        dispatch(showNofification({
                            type: 'success',
                            message: t('manga.form.success.create-chapter')
                        }));
                        props.onOk(res.data)
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


    let imageChapterSorter: Sortable;
    useEffect(() => {
        let imageChapterContainer: HTMLElement | null = document.getElementById('chapter-images-list');
        if (imageChapterContainer)
            imageChapterSorter = Sortable.create(imageChapterContainer);

        if (!props.chapterInput)
            resetChapterInput();
        else
            setChapterInput(props.chapterInput);

    }, [props.visible]);


    return <>
        <Modal width={'60%'} title={<h3 className="text-2xl">{props.title}</h3>}
            open={props.visible}
            onOk={() => { }}
            onCancel={() => props.onCancel()}
            footer={null}
        >
            <section className="py-[20px]">
                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px]'>
                    <span className='text-red-500'>*</span>
                    <span> Chapter name:</span>
                </label>
                <Input placeholder="enter your chapter name" value={chapterInput.name} onChange={val => setChapterInput({ ...chapterInput, name: val.target.value })} />
                {
                    <p className='text-[12px] text-red-500 px-[5px] m-0'>
                        {chapterInputError.chapterName && t(chapterInputError.chapterName)}
                    </p>
                }
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

                    <a className="mb-[10px] w-fit text-[12px]" onClick={uploadTextChapterByFile}>Upload text file (Support .txt)</a>

                    <div className=''>
                        <RichtextEditorForm onReady={onReadyMangaContentEditor} toolbarSettings={{}} />
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

                    <a className="mb-[10px] block text-[12px] underline" onClick={uploadImageChapter}>Upload image (maximum 30files), all files aren't format will be discard</a>

                    <div id="chapter-images-list" className="flex flex-wrap gap-[5px]">
                        {imageChapterFiles && imageChapterFiles.length > 0 && (
                            imageChapterFiles.map((item, index) => (
                                <CreateImageHtmlElement file={item} index={index} removeImage={deleteImageChapterFile} />
                                // <li key={index}>
                                //         <span>{file.name}</span>
                                //         <DeleteOutlined onClick={() => deleteImageChapterFile(index)} className="px-[5px] hover:text-red-500 cursor-pointer" />
                                //     </li>
                            ))
                        )}
                    </div>

                    {
                        <p className='text-[12px] text-red-500 px-[5px]  m-0'>
                            {chapterInputError.chapterImages && t(chapterInputError.chapterImages)}
                        </p>
                    }
                </section>
            }

            <div className="flex space-x-2 justify-end">
                <Button type="primary" className="w-fit mt-[10px]" onClick={resetChapterInput} loading={isCreatingChapter}>
                    {t('reset')}
                </Button>

                <Button type="primary" className="w-fit mt-[10px]" onClick={onCreateChapter} loading={isCreatingChapter}>
                    {t('manga.form.chapter.save-chapter-btn')}
                </Button>
            </div>

        </Modal>
    </>
}

export default MangaUploadChapterModal;
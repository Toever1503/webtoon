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
import chapterService from "../../../../services/manga/ChapterService";
import { ChapterType } from "../manga-form-new/ChapterSetting";

type MangaUploadChapterModalProps = {
    mangaInput: MangaInput,
    visible: boolean,
    onOk: (chapterInput: ChapterType) => void,
    onCancel: Function,
    title: string,
    chapterInput?: ChapterType,
    volumeId: string | number,
    refreshLatestChapter: Function,
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
    id?: number | string,
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
        console.log('fileRef: ', file);
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

                const allowType = ['text/plain'];
                if (file.size > 5000000) {
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
    const [oldImageChapterFiles, setOldImageChapterFiles] = useState<ImageChapterFileType[]>([]);
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
                let totalSize = 0;
                if ([...fileList].find((file: File) => !file.type.match('image.*'))) {
                    dispatch(showNofification({
                        type: 'error',
                        message: t('manga.form.errors.only-image-allowed'),
                    }));
                    return;
                }
                for (let i = 0; i < fileList.length; i++) {
                    totalSize += fileList[i].size;
                }
                if (totalSize > 100000000) {
                    dispatch(showNofification({
                        type: 'error',
                        message: t('manga.form.errors.total-file-size-exceed-100mb'),
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
                files.push(...imageChapterFiles);
                setImageChapterFiles(files);
                console.log('files add', imageChapterFiles);

            }
        };
        fileInput.click();
    };

    const deleteImageChapterFile = (index: number) => {
        const newFiles = imageChapterFiles?.filter((file: ImageChapterFileType, i) => i !== index);
        setImageChapterFiles(newFiles);
    }
    const deleteOldImageChapterFile = (index: number) => {
        const newFiles = oldImageChapterFiles?.filter((file: ImageChapterFileType, i) => i !== index);
        setOldImageChapterFiles(newFiles);
    }

    // begin chapter input
    const [chapterInput, setChapterInput] = useState<ChapterType>({
        id: '',
        chapterName: '',
        chapterIndex: 0,
        content: '',
        chapterImages: [],
        requiredVip: false,
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
            chapterName: '',
            chapterIndex: 0,
            content: '',
            chapterImages: [],
            requiredVip: false,
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
        setOldImageChapterFiles([]);
    }

    const [isCreatingChapter, setIsCreatingChapter] = useState<boolean>(false);
    const onCreateChapter = () => {
        console.log('on create chapter', imageChapterFiles);

        let errorCount = 0;
        const chapterContent = chapterContentEditorRef?.getHtml() || '';

        if (props.mangaInput.mangaType === 'TEXT') {
            if (!chapterInput.chapterName) {
                chapterInputError.chapterName = 'manga.form.errors.chapter-name-required';
                errorCount++;
            }
            else chapterInputError.chapterName = '';
        }

        if (props.mangaInput.mangaType === 'IMAGE') {
            if (oldImageChapterFiles.length + imageChapterFiles.length === 0) {
                chapterInputError.chapterImages = 'manga.form.errors.chapter-images-required';
                errorCount++;
            }
            else chapterInputError.chapterImages = '';
        }
        else if (props.mangaInput.mangaType === 'TEXT') {
            setChapterInput({
                ...chapterInput,
                content: chapterContent
            });
            if (!chapterContentEditorRef?.getText()) {
                chapterInputError.chapterContent = 'manga.form.errors.chapter-content-required';
                errorCount++;
            }
            else chapterInputError.chapterContent = '';
        }
        setChapterInputError({ ...chapterInputError });

        if (errorCount === 0) {
            console.log('adding chapter: ', chapterInput.volumeId);


            setIsCreatingChapter(true);
            const modalType = chapterInput.id ? 'edit' : 'create';

            if (props.mangaInput.mangaType === 'IMAGE') {
                console.log('create image chapter');

                const formdata = new FormData();
                if (chapterInput.id)
                    formdata.append('id', chapterInput.id.toString());
                formdata.append('chapterIndex', chapterInput?.chapterIndex?.toString() || '0');
                formdata.append('chapterName', chapterInput.chapterName);
                formdata.append('isRequiredVip', isRequireVipChapter.toString());
                formdata.append('volumeId', chapterInput?.volumeId?.toString() || '0');


                document.querySelectorAll('#chapter-images-list > div').forEach((item) => {
                    // @ts-ignore
                    if (item.chapterFile.id)
                        // @ts-ignore
                        formdata.append('files', new Blob(['']), 'id-' + item.chapterFile.id);
                    else
                        // @ts-ignore
                        formdata.append('files', item.chapterFile.file);
                });


                mangaService
                    .createImageChapter(formdata, props.mangaInput.id)
                    .then((res) => {
                        console.log('create image chapter success:', res.data);
                        props.onOk(res.data);
                        dispatch(showNofification({
                            type: 'success',
                            message: t(`manga.form.errors.${modalType}-success`)
                        }));
                        resetChapterInput();
                    })
                    .catch((err) => {
                        console.log('create image chapter error:', err);
                        dispatch(showNofification({
                            type: 'error',
                            message: t(`manga.form.errors.${modalType}-failed`)
                        }));
                    })
                    .finally(() => {
                        setIsCreatingChapter(false);
                    });

            }
            else if (props.mangaInput.mangaType === 'TEXT') {

                mangaService.createTextChapter({
                    id: chapterInput.id ? chapterInput.id : undefined,
                    chapterIndex: chapterInput.chapterIndex ? chapterInput.chapterIndex : 0,
                    chapterName: chapterInput.chapterName,
                    content: chapterContent,
                    requiredVip: isRequireVipChapter,
                    volumeId: chapterInput.volumeId || 0,
                    chapterImages: []
                }, props.mangaInput.id)
                    .then((res: AxiosResponse<ChapterType>) => {
                        console.log('create text chapter success:', res.data);
                        props.onOk(res.data);
                        dispatch(showNofification({
                            type: 'success',
                            message: t(`manga.form.errors.${modalType}-success`)
                        }));
                        resetChapterInput();
                    })
                    .catch((err) => {
                        console.log('create text chapter error:', err);
                        dispatch(showNofification({
                            type: 'error',
                            message: t(`manga.form.errors.${modalType}-failed`)
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


    let imageSorter: Sortable;
    useEffect(() => {
        console.log('use effect image sorter: ', props.mangaInput);

        if (props.chapterInput) {
            if (props.chapterInput.id)
                chapterService.findById(props.chapterInput.id)
                    .then((res: AxiosResponse<ChapterType>) => {
                        console.log('get detail chapter: ', res.data);

                        setChapterInput(res.data);
                        setMangaTextChapter(res.data.content);
                        if (res.data.chapterImages)
                            setOldImageChapterFiles(res.data.chapterImages.map((item:
                                // ChapterImageType 
                                any
                            ) => (
                                {
                                    id: item.id,
                                    index: item.imageIndex,
                                    file: '',
                                    data: '',
                                    url: item.image,
                                }
                            )));

                        // @ts-ignore
                        setIsRequireVipChapter(res.data.requiredVip);
                    })
                    .catch(err => {
                        console.log('get chapter error:', err);
                    })
        }
        else {
            resetChapterInput();
        }
        if (props.mangaInput.mangaType === 'IMAGE')
            // @ts-ignore
            imageSorter = Sortable.create(document.getElementById('chapter-images-list'));

    }, [props]);


    return <>
        <Modal width={'60%'} title={<h3 className="text-2xl">{props.title}</h3>}
            open={props.visible}
            onOk={() => { }}
            onCancel={() => props.onCancel()}
            footer={null}
        >
            <section className="py-[10px]">
                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px]'>
                    {
                        props.mangaInput.mangaType === 'TEXT' && <span className='text-red-500'>*</span>
                    }
                    <span> Tên chương:</span>
                </label>
                <Input placeholder="enter your chapter name" value={chapterInput.chapterName} onChange={val => setChapterInput({ ...chapterInput, chapterName: val.target.value })} />
                {
                    <p className='text-[12px] text-red-500 px-[5px] m-0'>
                        {chapterInputError.chapterName && t(chapterInputError.chapterName)}
                    </p>
                }
            </section>

            {
                !props.mangaInput.isFree &&
                <section className="flex items-center space-x-3 py-[10px]">
                    <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                        <span>Chương có yêu cầu trả phí?:</span>
                    </label>
                    <Checkbox className="mt-[5px]" checked={isRequireVipChapter} onChange={(val: CheckboxChangeEvent) => setIsRequireVipChapter(val.target.checked)} />
                </section>
            }

            {/* for text chapter content */}
            {
                props.mangaInput.mangaType === 'TEXT' &&
                <section >
                    <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px]'>
                        <span className='text-red-500'>*</span>
                        <span> Nội dung chương:</span>
                    </label>

                    <a className="mb-[10px] w-fit text-[12px]" onClick={uploadTextChapterByFile}>Tải nội dung bằng file (Hỗ trợ định dạng .txt, lưu ý file không dc quá 5mb và nội dung dưới 15000 từ)</a>

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
                        <span> Ảnh:</span>
                    </label>

                    <a className="mb-[10px] block text-[12px] underline" onClick={uploadImageChapter}>Lưu ý: chỉ được tối đa 30 ảnh(mỗi ảnh không quá 5mb), định dạng (jpa,png,webp) </a>

                    <div id="chapter-images-list" className="flex flex-wrap gap-[5px]">
                        {imageChapterFiles && imageChapterFiles.length > 0 && (
                            imageChapterFiles.map((item: ImageChapterFileType, index) => (
                                <CreateImageHtmlElement file={item} index={index} removeImage={deleteImageChapterFile} />
                                // <li key={index}>
                                //         <span>{file.name}</span>
                                //         <DeleteOutlined onClick={() => deleteImageChapterFile(index)} className="px-[5px] hover:text-red-500 cursor-pointer" />
                                //     </li>
                            ))
                        )}
                        {
                            oldImageChapterFiles.map((item: ImageChapterFileType, index) => (
                                <CreateImageHtmlElement file={item} index={index} removeImage={deleteOldImageChapterFile} />
                                // <li key={index}>
                                //         <span>{file.name}</span>
                                //         <DeleteOutlined onClick={() => deleteImageChapterFile(index)} className="px-[5px] hover:text-red-500 cursor-pointer" />
                                //     </li>
                            ))
                        }
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
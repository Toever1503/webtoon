import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { RichTextEditorComponent } from "@syncfusion/ej2-react-richtexteditor";
import { Button, Checkbox, Divider, Input, InputRef, message, Select, Space } from "antd";
import { CheckboxChangeEvent } from "antd/es/checkbox";
import { useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import RichtextEditorForm from "../../../../components/RichtextEditorForm";
import { MangaInput } from "../../../../services/MangaService";

type MangaUploadSingleChapterProps = {
    mangaInput: MangaInput,
}

interface ChapterInput {
    volume: number,
    chapterName: string,
    chapterIndex?: number,
    isRequireVip: boolean,
    chapterContent: string,
    chapterImages: File[],
}

interface ChapterInputError {
    chapterName: string,
    chapterContent: string,
    chapterImages: string,
}

const MangaUploadSingleChapter: React.FC<MangaUploadSingleChapterProps> = (props: MangaUploadSingleChapterProps) => {
    const { t } = useTranslation();
    // begin volume select
    const [volumeOptions, setVolumeOptions] = useState([
        {
            id: 1,
            name: 'Volume 1'
        },
    ]);

    const [isAddingNewVolume, setIsAddingNewVolume] = useState<boolean>(false);
    const [volumeVal, setVolumeVal] = useState<string>('');
    const addNewVolume = () => {
        console.log('add tag:', volumeVal);
        if (volumeVal) {
            setIsAddingNewVolume(true);
            setTimeout(() => {
                setVolumeOptions([...volumeOptions, {
                    id: volumeOptions.length + 1,
                    name: volumeVal
                }]);
                setVolumeVal('');
                setIsAddingNewVolume(false);
            }, 1000);
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

    const [imageChapterFiles, setImageChapterFiles] = useState<File[]>([]);
    const uploadImageChapter = () => {
        const fileInput = document.createElement('input');
        fileInput.type = 'file';
        fileInput.accept = '.png,.jpg,.jpeg,.zip';
        fileInput.multiple = true;
        fileInput.onchange = () => {
            const fileList: FileList | null = fileInput.files;
            const files: File[] = [];
            if (fileList) {
                for (let i = 0; i < fileList.length; i++) {
                    files.push(fileList[i]);
                }
                setImageChapterFiles(files)
            }
        };
        fileInput.click();
    };
    const deleteImageChapterFile = (index: number) => {
        const newFiles = imageChapterFiles?.filter((file: File, i) => i !== index);
        setImageChapterFiles(newFiles);
    }

    const [chapterInput, setChapterInput] = useState<ChapterInput>({
        volume: selectedVolume,
        chapterName: '',
        chapterIndex: 0,
        isRequireVip: false,
        chapterContent: '',
        chapterImages: [],
    });

    const [chapterInputError, setChapterInputError] = useState<ChapterInputError>({
        chapterName: '',
        chapterContent: '',
        chapterImages: '',
    });

    const [isCreatingChapter, setIsCreatingChapter] = useState<boolean>(false);
    const onCreateChapter = () => {
        console.log('on create chapter');

        let errorCount = 0;
        if (!chapterInput.chapterName) {
            setChapterInputError({
                ...chapterInputError,
                chapterName: 'manga.form.errors.chapter-name-required'
            });
            errorCount++;
        }
        if (props.mangaInput.mangaType === 'IMAGE') {
            if (chapterInput.chapterImages.length === 0) {
                setChapterInputError({
                    ...chapterInputError,
                    chapterImages: 'manga.form.errors.chapter-images-required'
                });
                errorCount++;
            }
        }
        else if (props.mangaInput.mangaType === 'TEXT') {
            if (!chapterInput.chapterContent) {
                setChapterInputError({
                    ...chapterInputError,
                    chapterContent: 'manga.form.errors.chapter-content-required'
                });
                errorCount++;
            }
        }


        if (errorCount === 0) {

        }
    }

    return (
        <div className="px-[15px] grid gap-y-[10px]">
            <section>
                <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                    <span> Volume:</span>
                </label>
                <Select
                    className="min-w-[180px]"
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

                <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                    <span className='text-red-500'>*</span>
                    <span> Chapter name:</span>
                </label>
                <Input placeholder="enter your chapter name" />
                {
                    <p className='text-[12px] text-red-500 px-[5px] m-0'>
                        {chapterInputError.chapterName && t(chapterInputError.chapterName)}
                    </p>
                }
            </section>

            <section>
                <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                    <span> Chapter Index(Optional):</span>
                </label>
                <Input placeholder="enter your chapter name" />
            </section>
            <section className="flex items-center space-x-3">
                <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                    <span> Require vip:</span>
                </label>
                <Checkbox className="mt-[5px]" checked={isRequireVipChapter} onChange={(val: CheckboxChangeEvent) => setIsRequireVipChapter(val.target.value)} />
            </section>

            {/* for text chapter content */}
            {
                props.mangaInput.mangaType === 'TEXT' &&
                <section >
                    <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
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
                    <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                        <span className='text-red-500'>*</span>
                        <span> Chapter Image:</span>
                    </label>
                    <a className="mb-[10px] block text-[12px]" onClick={uploadImageChapter}>Upload .zip folder or image file (support .png, jpg, jpeg), all files aren't format will be discard</a>

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
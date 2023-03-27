import { ArrowDownOutlined, DownOutlined } from "@ant-design/icons";
import { Button, Radio } from "antd";
import { useState } from "react";
import mangaService, { MangaInput } from "../../../services/MangaService";
import MangaChapterInfo from "./manga-form/MangaChapterInfo";
import MangaUploadSingleChapter from "./manga-form/MangaUploadSingleChapter";


type MangaChapterFormProps = {
    setMangaInput: Function,
    mangaInput: MangaInput
};
export type MangaType = 'TEXT' | 'IMAGE';

const MangaChapterForm: React.FC<MangaChapterFormProps> = (props: MangaChapterFormProps) => {

    type ChapterMenuType = 'INFO' | 'UPLOAD_SINGLE';

    const [mangaType, setMangaType] = useState<MangaType>('TEXT');
    const [chapterMenuValue, setChapterMenuValue] = useState<ChapterMenuType>('UPLOAD_SINGLE');

    const [hasConfirmedMangaType, setHasConfirmedMangaType] = useState<boolean>(false);
    const onChangeChapterMenu = (val: ChapterMenuType) => {
        setChapterMenuValue(val);
    }
    const confirmMangaType = () => {
        setHasConfirmedMangaType(true);
        console.log(mangaType);
        props.setMangaInput({
            ...props.mangaInput,
            mangaType: mangaType
        });
        props.mangaInput.id = '1';
        if (props.mangaInput.id)
            mangaService.setMangaType(props.mangaInput.id, mangaType)
                .then((res) => {
                    console.log('set manga type success ', res.data);
                })
                .catch(err => {
                    console.log('set manga type failed ', err);
                });
    }

    const [showChapterSetting, setShowChapterSetting] = useState<boolean>(false);


    return (
        <div className="bg-white w-full h-fit" style={{ border: '1px solid #c3c4c7' }}>
            <div style={{ borderBottom: '1px solid #c3c4c7' }} className='flex justify-between items-center p-[10px]'>
                <p className='text-[15px] font-bold m-0'>Chapter Setting</p>
                <DownOutlined className={`cursor-pointer ${showChapterSetting ? 'rotate-180' : ''}`} onClick={() => setShowChapterSetting(!showChapterSetting)} />
            </div>

            {
                showChapterSetting &&
                <>
                    {
                        !hasConfirmedMangaType ?
                            <section className='manga-type-choice px-[10px] text-center py-[15px] h-full pt-[50px]'>
                                <p className='text-[18px] font-bold mb-1'>Manga type:</p>
                                <span className="text-[12px] text-neutral-500">
                                    This setting cannot be changed after choosen.
                                </span>
                                <br />
                                <Radio.Group className="my-[20px]" value={mangaType} onChange={e => setMangaType(e.target.value)} >
                                    <Radio value={'TEXT'} className="text-[15px]">Text</Radio>
                                    <Radio value={'IMAGE'} className="text-[15px]">Image</Radio>
                                </Radio.Group>

                                <br />
                                <Button size="small" type="primary" onClick={confirmMangaType}>Next</Button>
                            </section>
                            :
                            <section className="chapter-info flex">
                                <div className="chapter-info-left w-[250px]">
                                    <a className={`block p-[10px] font-[500px] hover:bg-neutral-200 ease-in-out duration-200 ${(chapterMenuValue === 'INFO' ? 'bg-neutral-200' : '')}`} onClick={() => onChangeChapterMenu('INFO')} style={{ borderBottom: '1px solid #c3c4c79e' }}>Manga Chapter List</a>
                                    <a className={`block p-[10px] font-[500px] hover:bg-neutral-200 ease-in-out duration-200 ${(chapterMenuValue === 'UPLOAD_SINGLE' ? 'bg-neutral-200' : '')}`} onClick={() => onChangeChapterMenu('UPLOAD_SINGLE')} style={{ borderBottom: '1px solid #c3c4c79e' }}>Upload chapter</a>
                                </div>

                                <div className="chapter-info-right w-[calc(100%_-_250px)] h-full" style={{ borderLeft: '1px solid #c3c4c7' }}>
                                    {
                                        chapterMenuValue === 'INFO' && <MangaChapterInfo />
                                    }

                                    {
                                        chapterMenuValue === 'UPLOAD_SINGLE' && <MangaUploadSingleChapter mangaInput={props.mangaInput} />
                                    }
                                </div>
                            </section>
                    }
                </>
            }



        </div>)
};

export default MangaChapterForm;
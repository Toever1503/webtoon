import { ArrowDownOutlined, DownOutlined } from "@ant-design/icons";
import { Button, Radio } from "antd";
import { useState } from "react";
import mangaService, { MangaInput } from "../../../services/manga/MangaService";
import MangaChapterInfo from "./manga-form/MangaChapterSetting";
import MangaUploadChapterModal from "./manga-form/MangaUploadChapterModal";
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
        if (props.mangaInput.id)
            mangaService.setMangaType(props.mangaInput.id, mangaType)
                .then((res) => {
                    console.log('set manga type success ', res.data);
                })
                .catch(err => {
                    console.log('set manga type failed ', err);
                });
    }

    const [showChapterSetting, setShowChapterSetting] = useState<boolean>(true);


    return (
        <div className="bg-white w-full h-fit" style={{ border: '1px solid #c3c4c7' }}>
            <div style={{ borderBottom: '1px solid #c3c4c7' }} className='flex justify-between items-center p-[10px]'>
                <p className='text-[16px] font-bold m-0'>Chapter Setting</p>
                <DownOutlined className={`cursor-pointer ${showChapterSetting ? 'rotate-180' : ''}`} onClick={() => setShowChapterSetting(!showChapterSetting)} />
            </div>
            <section className="chapter-info ">
            <MangaChapterInfo mangaInput={props.mangaInput} />
            </section>
        </div>)
};

export default MangaChapterForm;
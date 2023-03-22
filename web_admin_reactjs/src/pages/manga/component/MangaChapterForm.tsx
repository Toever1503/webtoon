import { Button, Radio } from "antd";
import { useState } from "react";
import MangaChapterInfo from "./manga-form/MangaChapterInfo";
import MangaExtraInfo from "./manga-form/MangaExtraInfo";
import MangaUploadSingleChapter from "./manga-form/MangaUploadSingleChapter";

const MangaChapterForm: React.FC = () => {


    type MangaType = 'TEXT' | 'IMAGE';
    type ChapterMenuType = 'INFO' | 'UPLOAD_SINGLE';

    const [mangaType, setMangaType] = useState<MangaType>('TEXT');
    const [chapterMenuValue, setChapterMenuValue] = useState<ChapterMenuType>('INFO');

    const onChangeChapterMenu = (val: ChapterMenuType) => {
        setChapterMenuValue(val);
    }


    return (
        <div className="bg-white mt-[20px] min-w-[700px]" style={{ border: '1px solid #c3c4c7' }}>
            <p className='text-[15px] font-bold p-[10px] m-0' style={{ borderBottom: '1px solid #c3c4c7' }}>Chapter Setting</p>

            <section className='manga-type-choice px-[10px] text-center py-[15px]'>
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
                <Button size="small" type="primary">Next</Button>
            </section>

            <section className="chapter-info flex">
                <div className="chapter-info-left w-[280px] grid divide-y" >
                    <a className={`p-[10px] font-[500px] hover:bg-neutral-200 ease-in-out duration-200` + (chapterMenuValue === 'INFO' ? 'bg-neutral-200' : '')} onClick={() => onChangeChapterMenu('INFO')} style={{ borderBottom: '1px solid #c3c4c79e' }}>Manga Chapter List</a>
                    <a className={`p-[10px] font-[500px] hover:bg-neutral-200 ease-in-out duration-200` + (chapterMenuValue === 'UPLOAD_SINGLE' ? 'bg-neutral-200' : '')} onClick={() => onChangeChapterMenu('UPLOAD_SINGLE')} style={{ borderBottom: '1px solid #c3c4c79e' }}>Upload chapter</a>
                </div>

                <div className="chapter-info-right" style={{ borderLeft: '1px solid ##c3c4c7' }}>
                    {
                        chapterMenuValue === 'INFO' && <MangaChapterInfo />
                    }

                    {
                        chapterMenuValue === 'UPLOAD_SINGLE' && <MangaUploadSingleChapter />
                    }
                </div>
            </section>

        </div>)
};

export default MangaChapterForm;
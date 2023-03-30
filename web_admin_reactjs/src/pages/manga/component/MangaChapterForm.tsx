import { ArrowDownOutlined, DownOutlined } from "@ant-design/icons";
import { Button, Radio } from "antd";
import { useState } from "react";
import mangaService, { MangaInput } from "../../../services/manga/MangaService";
import { autoSaveMangaInfo } from "../AddNewManga";
import VolumeSetting from "./manga-form-new/VolumeSetting";
import MangaChapterSetting from "./manga-form/MangaChapterSetting";
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

        console.log(mangaType);
        props.setMangaInput({
            ...props.mangaInput,
            mangaType: mangaType
        });
        if (!props.mangaInput.id) {
            autoSaveMangaInfo(props.mangaInput)?.then((res) => {
                console.log('after set type: ', res);
                setHasConfirmedMangaType(true);
                mangaService.setMangaType(props.mangaInput.id, mangaType)
                    .then((res) => {
                        console.log('set manga type success ', res.data);
                    })
                    .catch(err => {
                        console.log('set manga type failed ', err);
                    });
            });
        }

    }

    const [showChapterSetting, setShowChapterSetting] = useState<boolean>(true);


    return (
        <div className="bg-white w-full h-fit" style={{ border: '1px solid #c3c4c7' }}>
            <div style={{ borderBottom: '1px solid #c3c4c7' }} className='flex justify-between items-center p-[10px]'>
                <p className='text-[16px] font-bold m-0'>Chapter Setting</p>
                <DownOutlined className={`cursor-pointer ${showChapterSetting ? 'rotate-180' : ''}`} onClick={() => setShowChapterSetting(!showChapterSetting)} />
            </div>
            {
                showChapterSetting &&
                <>
                    {
                        hasConfirmedMangaType ?
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
                            <section className="chapter-setting">
                                {/* <MangaChapterSetting mangaInput={props.mangaInput} /> */}
                                <VolumeSetting mangaInput={props.mangaInput} />
                            </section>
                    }
                </>
            }

        </div>)
};

export default MangaChapterForm;
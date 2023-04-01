import { ArrowDownOutlined, DownOutlined } from "@ant-design/icons";
import { Button, Popconfirm, Radio, Steps, Timeline } from "antd";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import mangaService, { MangaInput } from "../../../services/manga/MangaService";
import { showNofification } from "../../../stores/features/notification/notificationSlice";
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
    const { t } = useTranslation();
    const dispatch = useDispatch();

    type ChapterMenuType = 'INFO' | 'UPLOAD_SINGLE';
    const [currentStep, setCurrentStep] = useState<number>(2);
    const [mangaType, setMangaType] = useState<MangaType>('TEXT');
    const [displayType, setDisplayType] = useState<'VOL' | 'CHAP'>('CHAP');

    const confirmMangaType = () => {
        console.log(mangaType);
        props.setMangaInput({
            ...props.mangaInput,
            mangaType: mangaType
        });
        setCurrentStep(1);

    }

    const confirmDisplayType = async () => {
        props.mangaInput.displayType = displayType;
        if (!props.mangaInput.id) {
            // autoSaveMangaInfo(props.mangaInput)?.then((res) => {
            //     console.log('after set type: ', res);
            //     mangaService.setMangaTypeAndDisplayType(props.mangaInput.id, mangaType, displayType)
            //         .then((res) => {
            //             console.log('set manga type success ', res.data);
            //             setCurrentStep(2);
            //         })
            //         .catch(err => {
            //             console.log('set manga type failed ', err);
            //         });
            // });
            await autoSaveMangaInfo(props.mangaInput);
        }
        mangaService.setMangaTypeAndDisplayType(props.mangaInput.id, mangaType, displayType)
            .then((res) => {
                console.log('set manga type success ', res.data);
                setCurrentStep(2);
            })
            .catch(err => {
                console.log('set manga type failed ', err);
            });

    }

    const confirmChooseMangaTypeAgain = () => {

        mangaService.resetMangaType(props.mangaInput.id)
            .then(() => {
                setCurrentStep(0);
            })
            .catch(err => {
                console.log('reset manga type failed ', err);
                dispatch(showNofification({
                    type: "error",
                    message: "Có lỗi xảy ra, vui lòng thử lại!"
                }))
            })
    }
    const [showChapterSetting, setShowChapterSetting] = useState<boolean>(true);

    useEffect(() => {
        props.mangaInput.id = 1;
        props.mangaInput.mangaType = 'IMAGE';
        props.mangaInput.displayType = 'CHAP';
    }, [])

    return (
        <div className="bg-white w-full h-fit" style={{ border: '1px solid #c3c4c7' }}>
            <div style={{ borderBottom: '1px solid #c3c4c7' }} className='flex justify-between items-center p-[10px]'>
                <div className="flex space-x-3">
                    <p className='text-[16px] font-bold m-0'>Chapter Setting</p>
                    {
                        currentStep === 2 &&
                        <Popconfirm
                            title="Bạn sẽ phải nhập lại tất cả thông tin của truyện. Bạn có chắc chắn?"
                            onConfirm={confirmChooseMangaTypeAgain}
                            okText="Yes"
                            cancelText="No"
                        >
                            <Button size="small">Chọn lại loại truyện</Button>

                        </Popconfirm>
                    }
                </div>
                <DownOutlined className={`cursor-pointer ${showChapterSetting ? 'rotate-180' : ''}`} onClick={() => setShowChapterSetting(!showChapterSetting)} />
            </div>
            {
                currentStep === 0 &&
                <section className='manga-type-choice px-[10px] text-center py-[15px] h-full pt-[50px]'>
                    <p className='text-[16px] font-bold mb-1'>Manga type:</p>
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
            }

            {
                currentStep === 1 &&
                <section className='manga-type-choice px-[10px] text-center py-[15px] h-full pt-[30px]'>
                    <p className='text-[16px] font-bold mb-1'>Chọn loại hiển thị truyện:</p>
                    <span className="text-[12px] text-neutral-500">
                        This setting cannot be changed after choosen.
                    </span>
                    <br />
                    <Radio.Group className="my-[20px]" value={displayType} onChange={val => setDisplayType(val.target.value)}>
                        <Radio value={'CHAP'} className="text-[13px]">Theo chương</Radio>
                        <Radio value={'VOL'} className="text-[13px]">Theo tập</Radio>
                    </Radio.Group>

                    <div className="flex space-x-2 w-fit mx-auto">
                        {/* <Button size="small" type="primary" onClick={confirmDisplayType}>Quay lại</Button> */}
                        <Button size="small" type="primary" onClick={confirmDisplayType}>Tiếp theo</Button>
                    </div>
                </section>
            }

            {
                currentStep === 2 &&
                <section className="chapter-setting">
                    {/* <MangaChapterSetting mangaInput={props.mangaInput} /> */}
                    <VolumeSetting mangaInput={props.mangaInput} />
                </section>
            }


        </div>)
};

export default MangaChapterForm;
import { ArrowDownOutlined, DownOutlined } from "@ant-design/icons";
import { Button, Popconfirm, Radio, Steps, Timeline } from "antd";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import mangaService, { MangaInput } from "../../../services/manga/MangaService";
import { showNofification } from "../../../stores/features/notification/notificationSlice";
import { autoSaveMangaInfo } from "../AddEditMangaForm";
import VolumeSetting from "./manga-form-new/VolumeSetting";



type MangaChapterFormProps = {
    setMangaInput: Function,
    mangaInput: MangaInput,
    formType: 'ADD' | 'EDIT',
};
export type MangaType = 'TEXT' | 'IMAGE';

const MangaChapterForm: React.FC<MangaChapterFormProps> = (props: MangaChapterFormProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    type ChapterMenuType = 'INFO' | 'UPLOAD_SINGLE';
    const [currentStep, setCurrentStep] = useState<number>((props.formType === 'ADD' || !props.mangaInput.displayType) ? 0 : 2);
    const [mangaType, setMangaType] = useState<MangaType>('TEXT');
    const [displayType, setDisplayType] = useState<'VOL' | 'CHAP'>('CHAP');

    const confirmMangaType = () => {
        console.log(mangaType);
        setCurrentStep(1);
    }

    const confirmDisplayType = async () => {
        props.mangaInput.mangaType = mangaType;
        props.mangaInput.displayType = displayType;
        console.log('manga id: ', props.mangaInput.id);

        if (!props.mangaInput.id) {
            await autoSaveMangaInfo(props.mangaInput);
            console.log('auto save manga info');

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

    // props.mangaInput.id = 1;
    // props.mangaInput.mangaType = 'TEXT';
    // props.mangaInput.displayType = 'VOL';
    useEffect(() => {
        if (!props.mangaInput.displayType)
            confirmChooseMangaTypeAgain();
    }, [])

    return (
        <div className="bg-white w-full h-fit" style={{ border: '1px solid #c3c4c7' }}>
            <div style={{ borderBottom: '1px solid #c3c4c7' }} className='flex justify-between items-center p-[10px]'>
                <div className="flex space-x-3">
                    <p className='text-[16px] font-bold m-0'>Nội dung truyện</p>

                    {
                        props.formType === 'ADD' && currentStep === 2 &&
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
                    <p className='text-[16px] font-bold mb-1'>Loại truyện:</p>
                    <span className="text-[12px] text-neutral-500">
                        Bạn sẽ không thể thay đổi sau khi chọn.
                    </span>
                    <br />
                    <Radio.Group className="my-[20px]" value={mangaType} onChange={e => setMangaType(e.target.value)} >
                        <Radio value={'TEXT'} className="text-[15px]">Chữ</Radio>
                        <Radio value={'IMAGE'} className="text-[15px]">Ảnh</Radio>
                    </Radio.Group>

                    <br />
                    <Button size="small" type="primary" onClick={confirmMangaType}>Tiếp theo</Button>
                </section>
            }

            {
                currentStep === 1 &&
                <section className='manga-type-choice px-[10px] text-center py-[15px] h-full pt-[30px]'>
                    <p className='text-[16px] font-bold mb-1'>Chọn loại hiển thị truyện:</p>
                    <span className="text-[12px] text-neutral-500">
                        Bạn sẽ không thể thay đổi sau khi chọn.
                    </span>
                    <br />
                    <Radio.Group className="my-[20px]" value={displayType} onChange={val => setDisplayType(val.target.value)}>
                        <Radio value={'CHAP'} className="text-[13px]">Theo chương</Radio>
                        <Radio value={'VOL'} className="text-[13px]">Theo tập</Radio>
                    </Radio.Group>
                    <div className="max-w-[800px] mx-auto mb-[20px] rounded" style={{border: '1px solid #80808063'}}>
                        {
                            displayType === 'CHAP' ?
                                <img className="w-full h-full" src="/src/assets/chap-type-demo.jpg" />
                                :
                                <img className="w-full h-full" src="/src/assets/vol-type-demo.jpg" />
                        }

                    </div>

                    <div className="flex space-x-2 w-fit mx-auto">
                        <Button size="small" type="primary" onClick={() => setCurrentStep(0)}>Quay lại</Button>
                        <Button size="small" type="primary" onClick={confirmDisplayType}>Tiếp theo</Button>
                    </div>
                </section>
            }

            {
                currentStep === 2 &&
                <section className="chapter-setting">
                    <VolumeSetting mangaInput={props.mangaInput} />
                </section>
            }


        </div>)
};

export default MangaChapterForm;
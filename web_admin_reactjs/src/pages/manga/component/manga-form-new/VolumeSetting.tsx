import { NotificationOutlined } from "@ant-design/icons";
import { Badge, Button, Popconfirm, Select } from "antd";
import { AxiosResponse } from "axios";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import mangaService, { MangaInput } from "../../../../services/manga/MangaService";
import debounce from "../../../../utils/debounce";
import MangaAddEditVolumeModal, { VolumeForm } from "../modal/MangaAddEditVolumeModal";
import ChapterSetting from "./ChapterSetting";
import volumeService from "../../../../services/manga/VolumeService";
import { useDispatch } from "react-redux";
import { showNofification } from "../../../../stores/features/notification/notificationSlice";



type VolumeType = {
    id: string | number;
    name: string;
    volumeIndex: number;
}

type VolumeSettingProps = {
    mangaInput: MangaInput,
}
const VolumeSetting: React.FC<VolumeSettingProps> = (props: VolumeSettingProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const [lastVolIndex, setLastVolIndex] = useState<VolumeType>({
        volumeIndex: -1,
        name: '',
        id: ''
    });
    const [volumeData, setVolumeData] = useState<VolumeType[]>([]);
    const [selectedVolId, setSelectedVolId] = useState<number | string>('');
    const onSearchVol = debounce((val: string) => {
        console.log('search vol: ', val);
        onCallApiFilterVolume(val);
    })


    const onDeleteVolume = () => {
        console.log('delete vol: ', selectedVolId);
        volumeService.deleteById(selectedVolId)
            .then((res: AxiosResponse) => {
                console.log('delete vol success: ', res.data);
                setVolumeData(volumeData.filter((vol: VolumeType) => vol.id !== selectedVolId).map((vol: VolumeType) => {
                    if (volumeInput?.volumeIndex)
                        if (vol.volumeIndex > volumeInput.volumeIndex)
                            vol.volumeIndex--;
                    return vol;
                }));
                onCallApiLastVolIndex();
                setSelectedVolId('');
                dispatch(showNofification({
                    type: 'success',
                    message: t('manga.form.volume.delete-success'),
                }));
            })
            .catch(err => {
                console.log('delete vol failed: ', err);
                dispatch(showNofification({
                    type: 'error',
                    message: t('manga.form.volume.delete-failed'),
                }));
            });

    }
    // begin volume modal
    const [showAddEditVolumeModal, setShowAddEditVolumModal] = useState<boolean>(false);
    const [addEditVolumTitle, setAddEditVolumTitle] = useState<string>('');
    const [volumeInput, setVolumeInput] = useState<VolumeForm>();
    const openAddEditVolumeModal = (modalTitle: string, volume?: VolumeForm) => {
        console.log('on show add edit volume modal: ', lastVolIndex);

        if (volume) {
            setAddEditVolumTitle(`${modalTitle} (${t('manga.form.volume.volume')} ${volume.volumeIndex + 1})`);
            setVolumeInput(volume);
        }
        else {
            setVolumeInput(undefined);
            setAddEditVolumTitle(`${modalTitle} (${t('manga.form.volume.volume')} ${lastVolIndex.volumeIndex + 2})`);

        }

        setShowAddEditVolumModal(true);
    }
    const closeAddEditVolumeModal = () => {
        setTimeout(() => {
            setShowAddEditVolumModal(false);
        }, 50);
        setVolumeInput(undefined);
        console.log('modal status: ', showAddEditVolumeModal);
    }

    const onVolumeModalOk = (newVolume: VolumeForm) => {
        if (volumeInput)
            setVolumeData(volumeData.map((volume: VolumeType) => {
                if (volume.id === volumeInput.id)
                    return newVolume;
                return volume;
            }));
        else
            setVolumeData([newVolume, ...volumeData]);
        console.log('on add: ', newVolume);

        setLastVolIndex(newVolume);
        closeAddEditVolumeModal();
    }
    // end volume modal

    const onCallApiFilterVolume = (q: string) => {
        mangaService.filterVolume({
            mangaId: props.mangaInput.id,
            q: q ? isNaN(Number(q)) ? q : undefined : undefined,
            volumeIndex: q ? isNaN(Number(q)) ? null : Number(q) - 1 : null,
        }, 0, 10)
            .then((res) => {
                console.log('filter volume: ', res.data);

                setVolumeData(res.data.content);
            });
    }

    const onCallApiLastVolIndex = () => {
        mangaService.getLastVolIndex(props.mangaInput.id)
            .then((res: AxiosResponse<VolumeType>) => {
                console.log('last vol index: ', res.data);
                if (res.data)
                    setLastVolIndex(res.data);
            });
    }
    useEffect(() => {
        if (props.mangaInput.id) {
            onCallApiFilterVolume('');
            if (props.mangaInput.displayType === 'VOL')
                onCallApiLastVolIndex();
        }
    }, [props.mangaInput])

    return <div className="p-[10px]">
        <div className="flex space-x-2">
            <Badge dot>
                <NotificationOutlined style={{ fontSize: 16 }} />
            </Badge>
            {
                props.mangaInput.displayType === 'VOL' ?
                    <Badge >
                        {
                            !lastVolIndex.id ? <a>Hiện chưa có tập nào!</a> : <a>Tập mới nhất là {lastVolIndex.volumeIndex + 1}: {lastVolIndex.name}</a>
                        }
                    </Badge>
                    :
                    <Badge >
                        {
                            lastVolIndex.volumeIndex < 0 ? <a>Hiện chưa có chương nào!</a> : <a>Chương mới nhất là {lastVolIndex.volumeIndex + 1}</a>
                        }
                    </Badge>
            }

        </div>

        {
            props.mangaInput.displayType === 'VOL' ?
                <>
                    <div className="flex space-x-2 items-center mt-[15px]">
                        <label className="w-[100px]">Chọn tập: </label>
                        <Select
                            className="w-[300px]"
                            showSearch
                            value={selectedVolId}
                            placeholder='Tìm tập'
                            defaultActiveFirstOption={false}
                            filterOption={false}
                            onSearch={onSearchVol}
                            onChange={(val) => {
                                setSelectedVolId(val);
                                setVolumeInput(volumeData.find((vol) => vol.id === val));
                            }}
                            notFoundContent={<span className="inline-block text-center">Hiện chưa có tập nào!</span>}
                            options={(volumeData || []).map((d) => ({
                                value: d.id,
                                label: t('manga.form.volume.volume') + ` ${d.volumeIndex + 1}: ` + d.name,
                            }))}
                        />
                        <Button onClick={() => openAddEditVolumeModal(`${t('manga.form.volume.add-volume')}`)}>
                            Thêm tập mới
                            <MangaAddEditVolumeModal visible={showAddEditVolumeModal} onOk={onVolumeModalOk} onCancel={closeAddEditVolumeModal} title={addEditVolumTitle} mangaInput={props.mangaInput} volumeInput={volumeInput} />
                        </Button>

                        {
                            selectedVolId &&
                            <>
                                <Button onClick={() => openAddEditVolumeModal(`${t('manga.form.volume.edit-volume')}`, volumeInput)}>
                                    Chỉnh sửa tập
                                </Button>

                                <Popconfirm
                                    title={t('manga.form.volume.sure-delete')}
                                    onConfirm={onDeleteVolume}
                                    okText={t('confirm-yes')}
                                    cancelText={t('confirm-no')}
                                >
                                    <Button >
                                        Xóa tập
                                    </Button>
                                </Popconfirm>


                            </>
                        }

                    </div>
                    {
                        selectedVolId &&
                        <ChapterSetting mangaInput={props.mangaInput} volumeId={selectedVolId} isShowAddNewChapter={lastVolIndex.volumeIndex === volumeData.find((vol) => vol.id === selectedVolId)?.volumeIndex} />
                    }
                </>
                :
                <ChapterSetting mangaInput={props.mangaInput} volumeId={selectedVolId} refreshChapterLatest={setLastVolIndex} isShowAddNewChapter={lastVolIndex.volumeIndex === volumeData.find((vol) => vol.id === selectedVolId)?.volumeIndex} />
        }


    </div>
}


export default VolumeSetting;
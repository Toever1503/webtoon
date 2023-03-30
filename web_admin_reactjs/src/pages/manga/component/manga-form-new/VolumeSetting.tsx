import { NotificationOutlined } from "@ant-design/icons";
import { Badge, Button, Select } from "antd";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { MangaInput } from "../../../../services/manga/MangaService";
import debounce from "../../../../utils/debounce";
import MangaAddEditVolumeModal, { VolumeForm } from "../manga-form/MangaAddEditVolumeModal";
import ChapterSetting from "./ChapterSetting";


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

    const [lastVolIndex, setLastVolIndex] = useState<number>(-1);
    const [volumeData, setVolumeData] = useState<VolumeType[]>([]);
    const [selectedVolId, setSelectedVolId] = useState<number | string>('');
    const onSearchVol = debounce((val: string) => {
        console.log('search vol: ', val);
    })


    // begin volume modal
    const [showAddEditVolumeModal, setShowAddEditVolumModal] = useState<boolean>(false);
    const [addEditVolumTitle, setAddEditVolumTitle] = useState<string>('');
    const [volumeInput, setVolumeInput] = useState<VolumeForm>();
    const openAddEditVolumeModal = (modalTitle: string, volume?: VolumeForm) => {


        if (volume) {
            setAddEditVolumTitle(`${modalTitle} (${t('manga.form.volume.volume')} ${volume.volumeIndex + 1})`);
            setVolumeInput(volume);
        }
        else
            setAddEditVolumTitle(`${modalTitle} (${t('manga.form.volume.volume')} ${lastVolIndex + 1})`);

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
        closeAddEditVolumeModal();
    }
    // end volume modal

    props.mangaInput.id = 1;

    return <div className="p-[10px]">

        <div className="flex space-x-2 items-center">
            <label className="w-[100px]">Chọn tập: </label>
            <Select
                className="w-[300px]"
                showSearch
                value={selectedVolId}
                placeholder='Tìm tập'
                defaultActiveFirstOption={false}
                showArrow={false}
                filterOption={false}
                onSearch={onSearchVol}
                onChange={(val) => setSelectedVolId(val)}
                notFoundContent={<span className="inline-block text-center">Hiện chưa có tập nào!</span>}
                options={(volumeData || []).map((d) => ({
                    value: d.id,
                    label: t('manga.form.volume.volume') + ` ${d.volumeIndex + 1}` + d.name,
                }))}
            />
            <Button onClick={() => openAddEditVolumeModal(`${t('manga.form.volume.add-volume')}`)}>
                Thêm tập mới
                <MangaAddEditVolumeModal visible={showAddEditVolumeModal} onOk={onVolumeModalOk} onCancel={closeAddEditVolumeModal} title={addEditVolumTitle} mangaInput={props.mangaInput} volumeInput={volumeInput} />
            </Button>
            <div className="my-[5px] flex space-x-2">
                <Badge dot>
                    <NotificationOutlined style={{ fontSize: 16 }} />
                </Badge>
                <Badge >
                    {
                        lastVolIndex === -1 ? <a>Hiện chưa có tập nào!</a> : <a>Tập mới nhất là {lastVolIndex + 1}</a>
                    }
                </Badge>
            </div>
        </div>


        <ChapterSetting mangaInput={props.mangaInput} volumeId={selectedVolId} />
    </div>
}


export default VolumeSetting;
import { Button, Checkbox, Collapse, Select, Space, Timeline } from "antd";
import { AxiosResponse } from "axios";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import Sortable from "sortablejs";
import mangaService, { MangaInput } from "../../../../services/MangaService";
import MangaAddEditVolumeModal, { VolumeForm } from "./MangaAddEditVolumeModal";
import MangaUploadChapterModal from "./MangaUploadChapterModal";
import MangaVolumeInfoItem from "./MangaVolumeInfoItem";


const { Panel } = Collapse;

export type MangaVolumeType = {
    id: string | number;
    name: string;
    chapters: MangaChapterType[];
}

export type MangaChapterType = {
    id: string | number;
    name: string;
    chapterIndex?: number;
    content?: string
    images: MangaChapterImageType[];
    volume: MangaVolumeType;
}

export type MangaChapterImageType = {
    id: string | number;
    image: string;
    imageIndex?: number;
    chapter: MangaChapterType;
}

export type ChapterOptionType = {
    id: string | number;
    name: string;
}
type MangaChapterInfoProps = {
    mangaInput: MangaInput,
}



export type VolumeType = {
    id: string | number;
    name: string;
}

const MangaChapterInfo: React.FC<MangaChapterInfoProps> = (props: MangaChapterInfoProps) => {
    const { t } = useTranslation();


    const [volumeOptions, setVolumeOptions] = useState<VolumeType[]>([
    ]);



    // begin volume modal
    const [showAddEditVolumeModal, setShowAddEditVolumModal] = useState<boolean>(false);
    const [addEditVolumTitle, setAddEditVolumTitle] = useState<string>('');
    const [volumeInput, setVolumeInput] = useState<VolumeForm>();
    const openAddEditVolumeModal = (modalTitle: string, volume?: VolumeForm) => {
        setAddEditVolumTitle(modalTitle);
        setShowAddEditVolumModal(true);
    }
    const closeAddEditVolumeModal = () => {
        setTimeout(() => {
            setShowAddEditVolumModal(false);
        }, 50);
        console.log('modal status: ', showAddEditVolumeModal);
    }
    // end volume modal

    let volumeSortable: Sortable;
    // begin action
    useEffect(() => {

        // get volumes 
        mangaService
            .getVolumeForManga(1)
            .then((res: AxiosResponse<VolumeType[]>) => {
                console.log('res', res.data);
                setVolumeOptions(res.data.reverse());
            })
            .catch((err) => {
                console.log('get volume error:', err);
            });

        if (!volumeSortable)
            // @ts-ignore
            volumeSortable = Sortable.create(document.getElementById('volume-container'), {
                animation: 150,
                ghostClass: 'blue-background-class',
            });
    }, [])
    return (
        <div className="p-[15px]">
            <Button onClick={() => openAddEditVolumeModal('Add volume')}>
                Add volume
                <MangaAddEditVolumeModal visible={showAddEditVolumeModal} onOk={() => { 
                }} onCancel={closeAddEditVolumeModal} title={addEditVolumTitle} mangaInput={props.mangaInput} volumeInput={volumeInput} />
            </Button>

            <section id='volume-container' className="py-[20px] px-[10px] max-h-[500px] overflow-auto">
                {
                    volumeOptions.length > 0 &&
                    volumeOptions.map((volume: VolumeType, index: number) =>
                    (
                        <MangaVolumeInfoItem key={index} index={index} volume={volume} volumeSize={volumeOptions.length} mangaInput={props.mangaInput} editVolume={(volume: VolumeForm) =>  openAddEditVolumeModal('Add volume', volume)} />
                    ))
                }
            </section>
        </div>)
};


export default MangaChapterInfo;
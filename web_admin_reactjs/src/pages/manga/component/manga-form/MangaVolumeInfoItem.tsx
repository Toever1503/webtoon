import { DeleteOutlined, DownOutlined } from "@ant-design/icons";
import { Button, Checkbox, Collapse, Popconfirm } from "antd";
import { MouseEventHandler, ReactEventHandler, useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import Sortable from "sortablejs";
import { MangaInput } from "../../../../services/manga/MangaService";
import { VolumeForm } from "./MangaAddEditVolumeModal";
import { MangaVolumeType, VolumeType } from "./MangaChapterInfo";
import MangaUploadChapterModal from "./MangaUploadChapterModal";


type MangaVolumeInfoItemProps = {
    volume: VolumeType,
    index: number,
    volumeSize: number,
    mangaInput: MangaInput,
    editVolume: (volume: VolumeForm) => void
}
export type ChapterType = {
    id: string | number;
    name: string;
    chapterIndex?: number;
}


const MangaVolumeInfoItem: React.FC<MangaVolumeInfoItemProps> = (props: MangaVolumeInfoItemProps) => {
    const { t } = useTranslation();
    let index: number = props.index;
    // begin chapter modal
    const [showUploadChapterModal, setShowUploadChapterModal] = useState<boolean>(false);
    const [uploadChapterModalTitle, setUploadChapterModalTitle] = useState<string>('');
    // end chapter modal

    const volumeContainerRef = useRef<HTMLDivElement>(null);


    const onDeleleChapter = (chapter: ChapterType) => {

    }

    const showEditChapterModal = (e: any) => {
        console.log('e', e.target.className);
        if (e.target.className.indexOf('chapter-item') !== -1) {
    
            setShowUploadChapterModal(true);
            setUploadChapterModalTitle('Edit chapter');
        }
    }

    const [openPanel, setOpenPanel] = useState<boolean>(false);
    let chapterSortable: Sortable;
    useEffect(() => {
        // console.log('volumeContainerRef ', props.volume.id, document.querySelector('.volume-container-'+props.volume.id));
        if (!chapterSortable)
            // @ts-ignore
            chapterSortable = Sortable.create(volumeContainerRef.current, {
                onUpdate: (event: Sortable.SortableEvent) => {
                    console.log('event 1', event.to);
                    // @ts-ignore
                    let listChapters: HTMLElement[] = event.to.querySelectorAll('.chapter-item');
                    console.log('new index', listChapters);
                    event.newIndex
                },
            });
            props.mangaInput.mangaType = "IMAGE"
    }, [])

    return <>
        <div className="rounded mt-[10px]" style={{ border: '1px solid #c3c4c7' }}>
            <div className="flex justify-between items-center p-[10px]  cursor-pointer" onClick={() => setOpenPanel(!openPanel)} style={{ borderBottom: '1px solid #c3c4c7' }}>
                <div className="flex space-x-2 items-center">
                    <span>{t('manga.form.volume.volume') + ` ${props.volumeSize - index}`}: {props.volume.name}</span>
                    <Button onClick={(e) => {
                        e.stopPropagation();
                        props.editVolume({
                            id: props.volume.id,
                            name: props.volume.name,
                            volumeIndex: props.volume.volumeIndex,
                        });
                    }} size='small'>Edit vol</Button>

                    <Button onClick={(e) => {
                        e.stopPropagation();
                        setShowUploadChapterModal(true);
                        setUploadChapterModalTitle('Upload chapter');
                    }} size='small'>Add chapter</Button>
                    {/* 
                            <Button  onClick={(e) => {
                                e.stopPropagation();
                                console.log(e)
                            }}>more</Button> */}

                </div>
                <DownOutlined />
            </div>

            <div ref={volumeContainerRef} className={'py-[10px] px-[15px] duration-200 ease-in-out' + (openPanel ? '' : ' hidden')}>
                <div className="chapter-item flex justify-between items-center rounded cursor-pointer p-[5px] hover:bg-slate-200 ease-in-out duration-150" onClick={showEditChapterModal}>
                    <div className="chapter-title flex space-x-2 items-center">
                        <Checkbox />
                        <span>chap 0</span>
                    </div>
                    <Popconfirm
                        title={t('manga.form.chapter.sure-delete')}
                        onConfirm={(e) => {
                            e?.stopPropagation();
                            onDeleleChapter({} as ChapterType)
                        }}
                        okText={t('confirm-yes')}
                        cancelText={t('confirm-no')}
                    >
                        <DeleteOutlined onClick={e => e.stopPropagation()} className="hover:text-red-500" />
                    </Popconfirm>
                </div>

                <div className="chapter-item flex justify-between items-center rounded cursor-pointer p-[5px] hover:bg-slate-200 ease-in-out duration-150" onClick={showEditChapterModal}>
                    <div className="chapter-title flex space-x-2 items-center">
                        <Checkbox />
                        <span>chap 0</span>
                    </div>
                    <Popconfirm
                        title={t('manga.form.chapter.sure-delete')}
                        onConfirm={(e) => {
                            e?.stopPropagation();
                            onDeleleChapter({} as ChapterType)
                        }}
                        okText={t('confirm-yes')}
                        cancelText={t('confirm-no')}
                    >
                        <DeleteOutlined onClick={e => e.stopPropagation()} className="hover:text-red-500" />
                    </Popconfirm>
                </div>

            </div>
        </div>

        {
            showUploadChapterModal && <MangaUploadChapterModal visible={showUploadChapterModal} onOk={() => { }} onCancel={() => { setShowUploadChapterModal(false) }} title={uploadChapterModalTitle} mangaInput={props.mangaInput} />
        }

    </>
}
export default MangaVolumeInfoItem;
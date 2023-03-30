import { DeleteOutlined, DownOutlined } from "@ant-design/icons";
import { Button, Checkbox, Collapse, Popconfirm } from "antd";
import { AxiosResponse } from "axios";
import { MouseEventHandler, ReactEventHandler, useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import Sortable from "sortablejs";
import mangaService, { MangaInput } from "../../../../services/manga/MangaService";
import ChapterInfoItem from "./ChapterInfoItem";
import { VolumeForm } from "./MangaAddEditVolumeModal";
import { MangaVolumeType, VolumeType } from "./MangaChapterSetting";
import MangaUploadChapterModal from "./MangaUploadChapterModal";


type MangaVolumeInfoItemProps = {
    volume: VolumeType,
    index: number,
    volumeSize: number,
    mangaInput: MangaInput,
    editVolume: (volume: VolumeForm) => void
}
export type ChapterType = {
    id?: string | number;
    name: string;
    chapterIndex?: number;
    isRequiredVip: boolean;
    content?: string
    volumeId?: string | number;
    chapterImages?: ChapterImageType[];
}

type ChapterImageType = {
    id: string | number;
    image: string;
    imageIndex: number;
}

const MangaVolumeInfoItem: React.FC<MangaVolumeInfoItemProps> = (props: MangaVolumeInfoItemProps) => {
    const { t } = useTranslation();
    let index: number = props.index;
    // begin chapter modal
    const [showUploadChapterModal, setShowUploadChapterModal] = useState<boolean>(false);
    const [uploadChapterModalTitle, setUploadChapterModalTitle] = useState<string>('');
    const [chapterInput, setChapterInput] = useState<ChapterType>();

    const onAddEditChapterOk = (chapter: ChapterType) => {
        console.log('on ok chapter modal ', chapter);
        if (chapterInput)
            setChapterData(chapterData.map((item) => item.id === chapterInput.id ? chapter : item));
        else
            setChapterData([chapter, ...chapterData]);
        setShowUploadChapterModal(false);
    }
    // end chapter modal

    const showEditChapterModal = (e: any, chapter: ChapterType) => {
        console.log('e', e.target.className);
        if (e.target.className.indexOf('chapter-item') !== -1) {
            setShowUploadChapterModal(true);
            setUploadChapterModalTitle(`${t('manga.form.chapter.edit-chapter-btn')} (${t('manga.form.chapter.chapter')} ${(chapter.chapterIndex || 0)+1})`);
            setChapterInput(chapter);
        }
    }


    const [chapterData, setChapterData] = useState<ChapterType[]>([]);
    const volumeContainerRef = useRef<HTMLDivElement>(null);

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

        console.log('volume', props.volume);

        mangaService.getChapterByVolumeId(props.volume.id)
            .then((res: AxiosResponse<ChapterType[]>) => {
                setChapterData(res.data);
                // console.log('chapter data', res);
            })
            .catch((err) => { })

        props.mangaInput.mangaType = "TEXT";
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
                        setUploadChapterModalTitle(`${t('manga.form.chapter.add-chapter')} (${t('manga.form.chapter.chapter')} ${chapterData.length + 1})`);

                    }} size='small'>
                        {t('manga.form.chapter.add-chapter-btn')}
                    </Button>
                    {/* 
                            <Button  onClick={(e) => {
                                e.stopPropagation();
                                console.log(e)
                            }}>more</Button> */}

                </div>
                <DownOutlined />
            </div>

            <div ref={volumeContainerRef} className={'py-[10px] px-[15px] duration-200 ease-in-out' + (openPanel ? '' : ' hidden')}>

                {
                    chapterData.length > 0 ? chapterData.map((chapter: ChapterType, index: number) => (
                        <ChapterInfoItem key={index} mangaInput={props.mangaInput} chapter={chapter} index={index} showEditChapterModal={showEditChapterModal} />
                    ))
                        :
                        <>{t('manga.form.chapter.empty-chapter')}</>
                }
            </div>
        </div>

        {
            showUploadChapterModal && <MangaUploadChapterModal
                visible={showUploadChapterModal}
                onOk={onAddEditChapterOk}
                onCancel={() => { setShowUploadChapterModal(false) }}
                title={uploadChapterModalTitle}
                mangaInput={props.mangaInput}
                chapterInput={chapterInput}
                volumeId={props.volume.id}
            />
        }

    </>
}
export default MangaVolumeInfoItem;
import { DeleteOutlined } from "@ant-design/icons";
import { Checkbox, Popconfirm } from "antd";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { MangaInput } from "../../../../services/manga/MangaService";
import { ChapterType } from "./MangaVolumeInfoItem";


type ChapterInfoItemProps = {
    chapter: ChapterType;
    mangaInput: MangaInput;
    index: number;
    showEditChapterModal: (e: any, chapter: ChapterType) => void;
}

const ChapterInfoItem: React.FC<ChapterInfoItemProps> = (props: ChapterInfoItemProps) => {
    const { t } = useTranslation();

    const onDeleleChapter = (chapter: ChapterType) => {
    }

    return <>
        <div className="chapter-item flex justify-between items-center rounded cursor-pointer p-[5px] hover:bg-slate-200 ease-in-out duration-150" onClick={(e) => props.showEditChapterModal(e, props.chapter)}>
            <div className="chapter-title flex space-x-2 items-center">
                <Checkbox />
                <span>
                    {t('manga.form.chapter.chapter')} {(props.chapter.chapterIndex || 0) + 1}: {props.chapter.chapterName}
                </span>
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
    </>
};

export default ChapterInfoItem;
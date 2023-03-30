

import { DeleteOutlined } from "@ant-design/icons";
import { Checkbox, Popconfirm, TablePaginationConfig } from "antd";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { MangaInput } from "../../../../services/manga/MangaService";
import { ChapterType } from "../manga-form/MangaVolumeInfoItem";


type ChapterItemProps = {
    chapterInput: ChapterType;
    mangaInput: MangaInput;
    showEditChapterModal: (e: any, chapter: ChapterType) => void;
}

const ChapterItem: React.FC<ChapterItemProps> = (props: ChapterItemProps) => {
    const { t } = useTranslation();

    const onDeleleChapter = (chapter: ChapterType) => {
    }

 

    return <>
        <div className="chapter-item flex justify-between items-center rounded cursor-pointer p-[5px] hover:bg-slate-200 ease-in-out duration-150" onClick={(e) => props.showEditChapterModal(e, props.chapterInput)}>
            <div className="chapter-title flex space-x-2 items-center">
                <span>
                    {t('manga.form.chapter.chapter')} {(props.chapterInput.chapterIndex || 0) + 1}: {props.chapterInput.name}
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

export default ChapterItem;
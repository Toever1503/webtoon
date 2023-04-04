

import { DeleteOutlined } from "@ant-design/icons";
import { Checkbox, Popconfirm, TablePaginationConfig } from "antd";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { MangaInput } from "../../../../services/manga/MangaService";
import chapterService from "../../../../services/manga/ChapterService";
import { useDispatch } from "react-redux";
import { showNofification } from "../../../../stores/features/notification/notificationSlice";
import { ChapterType } from "./ChapterSetting";


type ChapterItemProps = {
    chapterInput: ChapterType;
    mangaInput: MangaInput;
    showEditChapterModal: (e: any, chapter: ChapterType) => void;
    onDeleteOk: () => void;
}

const ChapterItem: React.FC<ChapterItemProps> = (props: ChapterItemProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const onDeleleChapter = () => {
        if (props.chapterInput.id)
            chapterService.deleteById(props.chapterInput.id)
                .then((res) => {
                    console.log('delete chapter res: ', res);
                    dispatch(showNofification({
                        type: 'success',
                        message: t('manga.form.chapter.delete-success')
                    }));
                    props.onDeleteOk();
                })
                .catch((err) => {
                    console.log('delete chapter err: ', err);
                    dispatch(showNofification({
                        type: 'error',
                        message: t('manga.form.chapter.delete-failed')
                    }));
                });
    }



    return <>
        <div className="chapter-item flex justify-between items-center rounded cursor-pointer p-[5px] hover:bg-slate-200 ease-in-out duration-150" onClick={(e) => props.showEditChapterModal(e, props.chapterInput)}>
            <div className="chapter-title flex space-x-2 items-center">
                <span>
                    {t('manga.form.chapter.chapter')} {(props.chapterInput.chapterIndex || 0) + 1}: {props.chapterInput.chapterName}
                </span>
            </div>
            <Popconfirm
                title={t('manga.form.chapter.sure-delete')}
                onConfirm={(e) => {
                    e?.stopPropagation();
                    onDeleleChapter();
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
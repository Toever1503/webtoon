import { NotificationOutlined } from "@ant-design/icons";
import { Badge, Button, List, Pagination, Select, Skeleton, Spin, TablePaginationConfig } from "antd";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { MangaInput } from "../../../../services/manga/MangaService";
import debounce from "../../../../utils/debounce";
import MangaUploadChapterModal from "../manga-form/MangaUploadChapterModal";
import { ChapterType } from "../manga-form/MangaVolumeInfoItem";
import ChapterItem from "./ChapterItem";


type ChapterSettingProps = {
    mangaInput: MangaInput,
    volumeId: number | string
}
const ChapterSetting: React.FC<ChapterSettingProps> = (props: ChapterSettingProps) => {
    const { t } = useTranslation();

    const [lastChapterIndex, setLastChapterIndex] = useState<number>(-1);
    const [chapterData, setChapterData] = useState<ChapterType[]>([]);
    const [searchChapterVal, setSearchChapterVal] = useState<string>('');
    const onSearchonSearchChapter = debounce((val: string) => {
        console.log('search chapter: ', val);
    });
    const onChangeChapterSelect = (value: string) => { };




    // begin chapter modal
    const [showUploadChapterModal, setShowUploadChapterModal] = useState<boolean>(false);
    const [uploadChapterModalTitle, setUploadChapterModalTitle] = useState<string>('');
    const [chapterInput, setChapterInput] = useState<ChapterType>({
        id: '',
        name: '',
        chapterIndex: 0,
        volumeId: '',
        isRequiredVip: false,
    });

    const onAddEditChapterOk = (chapter: ChapterType) => {
        console.log('on ok chapter modal ', chapter);
        if (chapterInput)
            setChapterData(chapterData.map((item) => item.id === chapterInput.id ? chapter : item));
        else
            setChapterData([chapter, ...chapterData]);
        setShowUploadChapterModal(false);
    }
    const onShowEditChapterModal = (e: any, chapter: ChapterType) => {
        console.log('e', e.target.className);
        if (e.target.className.indexOf('chapter-item') !== -1) {
            setShowUploadChapterModal(true);
            setUploadChapterModalTitle(`${t('manga.form.chapter.edit-chapter-btn')} (${t('manga.form.chapter.chapter')} ${(chapter.chapterIndex || 0) + 1})`);
            setChapterInput(chapter);
        }
    }
    // end chapter modal


    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    return <div className="mt-[20px]">
        <div className="flex space-x-2 items-center">
            <label className="w-[100px]">Tìm chương: </label>
            <Select
                className="w-[300px]"
                showSearch
                value={searchChapterVal}
                placeholder='Tìm tập'
                defaultActiveFirstOption={false}
                showArrow={false}
                filterOption={false}
                onSearch={onSearchonSearchChapter}
                onChange={onChangeChapterSelect}
                notFoundContent={<span className="inline-block text-center">Hiện chưa có chương nào!</span>}
                options={(chapterData || []).map((d) => ({
                    value: d.id,
                    label: t('manga.form.volume.volume') + ` ${(d.chapterIndex || 0) + 1}` + d.name,
                }))}
            />
            <Button onClick={(e) => {
                setShowUploadChapterModal(true);
                setUploadChapterModalTitle(`${t('manga.form.chapter.add-chapter')} (${t('manga.form.chapter.chapter')} ${chapterData.length + 1})`);

            }} >
                {t('manga.form.chapter.add-chapter-btn')}
            </Button>
        </div>

        <div className="rounded mt-[15px]">
            <List
                header={<div>Danh sách chương:</div>}
                footer={
                    <>
                        <div className="mx-auto w-fit">
                            <Pagination current={pageConfig.current} pageSize={pageConfig.pageSize} showSizeChanger={false} total={pageConfig.total} />
                        </div>
                    </>
                }
                // loading={true}
                bordered
                dataSource={[]}
                renderItem={(item) => (
                    <div className="p-[10px]">
                        {/* <ChapterItem chapterInput={chapterInput} mangaInput={props.mangaInput} showEditChapterModal={onShowEditChapterModal} /> */}
                    </div>

                )}
            />
        </div>
        {
            showUploadChapterModal && <MangaUploadChapterModal
                visible={showUploadChapterModal}
                onOk={onAddEditChapterOk}
                onCancel={() => { setShowUploadChapterModal(false) }}
                title={uploadChapterModalTitle}
                mangaInput={props.mangaInput}
                chapterInput={chapterInput}
                volumeId={props.volumeId}
            />
        }


    </div>
}

export default ChapterSetting;
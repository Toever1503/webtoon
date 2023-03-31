import { NotificationOutlined } from "@ant-design/icons";
import { Badge, Button, Input, List, Pagination, Select, Skeleton, Spin, TablePaginationConfig } from "antd";
import { AxiosResponse } from "axios";
import { ChangeEvent, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import chapterService from "../../../../services/manga/ChapterService";
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
    const [isSearching, setIsSearching] = useState<boolean>(false);
    const [searchChapterVal, setSearchChapterVal] = useState<string>('');
    const onSearchonSearchChapter = debounce(() => {
        console.log('search chapter: ', searchChapterVal);
    });


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

    const callApiSearchChapter = (page: number, pageSize: number) => { };

    useEffect(() => {

        chapterService.filterChapter({
            volumeId: props.volumeId,
        }, 0, 10)
            .then((res: AxiosResponse<{
                content: ChapterType[],
            }>) => {
                console.log('filter chapter: ', res);
                setChapterData(res.data.content);
            });
    }, []);

    return <div className="mt-[20px]">


        <div className="rounded mt-[15px]">
            <List
                header={
                    <div>
                        <div className="flex space-x-2 items-center justify-between">
                            <div className="flex space-x-2 items-center">
                                <label className="mr-[10px]">Danh sách chương: </label>
                                <Button onClick={(e) => {
                                    setShowUploadChapterModal(true);
                                    setUploadChapterModalTitle(`${t('manga.form.chapter.add-chapter')} (${t('manga.form.chapter.chapter')} ${chapterData.length + 1})`);

                                }} >
                                    {t('manga.form.chapter.add-chapter-btn')}
                                </Button>
                            </div>

                            <div>
                                {/* <Select
                                    className="w-[300px]"
                                    showSearch
                                    value={searchChapterVal}
                                    placeholder="Tìm tập"
                                    defaultActiveFirstOption={false}
                                    filterOption={false}

                                   
                                    notFoundContent={<span className="inline-block text-center">Hiện chưa có chương nào!</span>}
                                    options={(chapterData || []).map((d) => ({
                                        value: d.id,
                                        label: t('manga.form.volume.volume') + ` ${(d.chapterIndex || 0) + 1}` + d.name,
                                    }))}
                                /> */}

                                <Input.Search placeholder="Tìm tập" value={searchChapterVal} onChange={e => setSearchChapterVal(e.target.value)} onSearch={onSearchonSearchChapter} />
                            </div>
                        </div>
                    </div>}
                footer={
                    <>
                        <div className="mx-auto w-fit">
                            <Pagination current={pageConfig.current} pageSize={pageConfig.pageSize} showSizeChanger={false} total={pageConfig.total} />
                        </div>
                    </>
                }
                // loading={true}
                bordered
                loading={isSearching}
                dataSource={[]}
                renderItem={(item) => (
                    <div className="p-[10px]">
                        <ChapterItem chapterInput={chapterInput} mangaInput={props.mangaInput} showEditChapterModal={onShowEditChapterModal} />
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
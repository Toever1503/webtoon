import { NotificationOutlined } from "@ant-design/icons";
import { Badge, Button, Input, List, Pagination, Select, Skeleton, Spin, TablePaginationConfig } from "antd";
import { AxiosResponse } from "axios";
import { ChangeEvent, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import chapterService from "../../../../services/manga/ChapterService";
import { MangaInput } from "../../../../services/manga/MangaService";
import debounce from "../../../../utils/debounce";
import ChapterItem from "./ChapterItem";
import MangaUploadChapterModal from "../modal/MangaUploadChapterModal";


export type ChapterType = {
    id?: string | number;
    chapterName: string;
    chapterIndex?: number;
    requiredVip: boolean;
    content?: string
    volumeId?: string | number;
    chapterImages: ChapterImageType[];
}
type ChapterImageType = {
    id: string | number;
    image: string;
    imageIndex: number;
}

type ChapterSettingProps = {
    mangaInput: MangaInput,
    volumeId: number | string,
    isShowAddNewChapter: boolean,
    refreshChapterLatest: Function
}
const ChapterSetting: React.FC<ChapterSettingProps> = (props: ChapterSettingProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const [lastChapterIndex, setLastChapterIndex] = useState<number>(-1);
    const [chapterData, setChapterData] = useState<ChapterType[]>([]);
    const [isSearching, setIsSearching] = useState<boolean>(false);
    const [searchChapterVal, setSearchChapterVal] = useState<string>('');
    const onSearchonSearchChapter = debounce(() => {
        console.log('search chapter: ', searchChapterVal);
        setIsSearching(true);
        onCallApiSearch((pageConfig.current || 1) - 1, pageConfig.pageSize);
    });


    // begin chapter modal
    const [showUploadChapterModal, setShowUploadChapterModal] = useState<boolean>(false);
    const [uploadChapterModalTitle, setUploadChapterModalTitle] = useState<string>('');
    const [chapterInput, setChapterInput] = useState<ChapterType>({
        id: '',
        chapterName: '',
        chapterIndex: 0,
        volumeId: props.volumeId,
        requiredVip: false,
        chapterImages: [],
    });

    const onAddEditChapterOk = (chapter: ChapterType) => {
        console.log('on ok chapter modal ', chapter);
        if (chapterInput.id)
            setChapterData(chapterData.map((item) => item.id === chapterInput.id ? chapter : item));
        else {
            chapterData.unshift(chapter);
            setChapterData(chapterData);

            if (props.mangaInput.displayType === 'CHAP')
                if (props.refreshChapterLatest)
                    props.refreshChapterLatest({
                        name: '',
                        volumeIndex: chapter.chapterIndex,
                    });

            setLastChapterIndex(lastChapterIndex + 1);

            console.log('chapter index-1: ', lastChapterIndex);

        }

        setChapterInput({
            id: '',
            chapterName: '',
            chapterIndex: 0,
            volumeId: props.volumeId,
            requiredVip: false,
            chapterImages: [],
        })
        setShowUploadChapterModal(false);

    }
    const onShowEditChapterModal = (e: any, chapter: ChapterType) => {
        // console.log('e', e.target.className);

        if (e.target.className.indexOf('chapter-item') !== -1) {
            setShowUploadChapterModal(true);
            setUploadChapterModalTitle(`${t('manga.form.chapter.edit-chapter-btn')} (${t('manga.form.chapter.chapter')} ${(chapter.chapterIndex || 0) + 1})`);
            setChapterInput(chapter);
            console.log('edit chapter: ', chapter);

        }
    }
    const showAddChapterModal = () => {
        console.log('last chapter index- ', lastChapterIndex);
        setChapterInput({
            id: '',
            chapterName: '',
            chapterIndex: 0,
            volumeId: props.volumeId,
            requiredVip: false,
            chapterImages: [],
        });

        setShowUploadChapterModal(true);
        setUploadChapterModalTitle(`${t('manga.form.chapter.add-chapter')} (${t('manga.form.chapter.chapter')} ${lastChapterIndex + 2})`);
    };
    // end chapter modal


    const [pageConfig, setPageConfig] = useState<TablePaginationConfig>({
        current: 1,
        pageSize: 10,
        total: 0,
    });


    const onChangingPage = (page: number) => {
        console.log('on change page');
        pageConfig.current = page;
        onCallApiSearch(page - 1, pageConfig.pageSize);
    }

    const onCallApiSearch = (page: number = 0, size: number = 10) => {
        console.log('on call api search');
        chapterService.filterChapter({
            volumeId: props.mangaInput.displayType === "VOL" ? props.volumeId : null,
            mangaId: props.mangaInput.displayType === "VOL" ? null : props.mangaInput.id,
            q: searchChapterVal ? isNaN(Number(searchChapterVal)) ? searchChapterVal : undefined : undefined,
            displayType: props.mangaInput.displayType,
            chapterIndex: searchChapterVal ? isNaN(Number(searchChapterVal)) ? null : Number(searchChapterVal) - 1 : null,
        }, page, size)
            .then((res: AxiosResponse<{
                totalElements: number | undefined;
                content: ChapterType[],
            }>) => {
                console.log('filter chapter: ', res);
                setChapterData(res.data.content);
                setPageConfig({
                    ...pageConfig,
                    total: res.data.totalElements,
                });
            })
            .finally(() => {
                setIsSearching(false);
            });
    }

    useEffect(() => {
        if (props.mangaInput.id) {
            onCallApiSearch();
            if (props.mangaInput.displayType === 'CHAP')
                chapterService.getLastChapterIndexForChapType(props.mangaInput.id)
                    .then((res) => {
                        console.log('last chapter index: ', res.data);
                        if (res.data)
                            setLastChapterIndex(res.data.chapterIndex);

                    });
            else
                chapterService.getLastChapterIndexForVolType(props.volumeId)
                    .then((res: AxiosResponse<number>) => {
                        setLastChapterIndex(res.data);
                    });
        }

    }, [props]);

    return <div className="mt-[20px]">


        <div className="rounded mt-[15px]">
            <List
                header={
                    <div>
                        <div className="flex space-x-2 items-center justify-between">
                            <div className="flex space-x-2 items-center">
                                <label className="mr-[10px]">Danh sách chương: </label>

                                {
                                    props.mangaInput.displayType === 'VOL' ?
                                        props.isShowAddNewChapter && <Button onClick={showAddChapterModal} >
                                            {t('manga.form.chapter.add-chapter-btn')}
                                        </Button>
                                        :
                                        <Button onClick={showAddChapterModal}>
                                            {t('manga.form.chapter.add-chapter-btn')}
                                        </Button>
                                }

                            </div>

                            <div>
                                <Input.Search placeholder="Tìm chương" value={searchChapterVal} onChange={e => setSearchChapterVal(e.target.value)} onSearch={onSearchonSearchChapter} />
                            </div>
                        </div>
                    </div>}
                footer={
                    <>
                        <div className="mx-auto w-fit">
                            <Pagination current={pageConfig.current} pageSize={pageConfig.pageSize} showSizeChanger={false} onChange={onChangingPage} total={pageConfig.total} />
                        </div>
                    </>
                }
                bordered
                loading={isSearching}
                dataSource={chapterData}
                renderItem={(item: ChapterType) => (
                    <div className="p-[10px]">
                        <ChapterItem chapterInput={item} mangaInput={props.mangaInput} onDeleteOk={() => {
                            setChapterData(chapterData.filter((i: ChapterType) => i.id !== item.id).map((i: ChapterType) => {
                                if (i?.chapterIndex)
                                    // @ts-ignore
                                    if (i.chapterIndex > item.chapterIndex)
                                        i.chapterIndex--;
                                return i;
                            }));
                            props.refreshChapterLatest();
                        }} showEditChapterModal={onShowEditChapterModal} />
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
                refreshLatestChapter={props.refreshChapterLatest}
            />
        }


    </div>
}

export default ChapterSetting;
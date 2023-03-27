import { Button, Checkbox, Collapse, Select, Space, Timeline } from "antd";
import { useState } from "react";


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

const MangaChapterInfo: React.FC = () => {
    const [chapterOptions, setChapterOptions] = useState<ChapterOptionType[]>([
        {
            id: 'CHAP1',
            name: 'Chapter 1',
        },
        {
            id: 'CHAP2',
            name: 'Chapter 2',
        }
    ]);
    const [selectedChapter, setSelectedChapter] = useState<number | string | null>(null);

    const onSelectedChapter = (val: number | string) => {
        console.log(val);
        setSelectedChapter(val);
    };

    const onChange = (key: string | string[]) => {
        console.log(key);
    };

    const [volumeOriginalData, setVolumeOriginalData] = useState([
        {
            id: 'VOL1',
            name: 'Volume 1',
            chapters: [
                {
                    id: 'CHAP1',
                    name: 'Chapter 1',
                    chapterIndex: 1,
                    images: [
                        {
                            id: 'IMG1',
                            image: 'https://i.pinimg.co',
                            imageIndex: 1,
                            chapter: {
                                id: 'CHAP1',
                                name: 'Chapter 1',
                                chapterIndex: 1,
                                images: [],
                            }
                        }
                    ],
                    volume: {
                        id: 'VOL1',
                        name: 'Volume 1',
                        chapters: []
                    }
                }
            ]
        }]);
    const [volumeData, setVolumeData] = useState([]);


    // begin action
    const [actionVal, setActionVal] = useState('NONE');
    const [moveVolumeVal, setMoveVolumeVal] = useState('VOL1');

    const onClickApply = () => {
        console.log('apply');
        switch (actionVal) {
            case 'MOVE':
                console.log('move to volume: ', moveVolumeVal);
                break;
            case 'DELETE':
                console.log('delete');
                break;
            default:
                break;
        }
    };
    return (
        <div className="px-[15px]">
            <section>
                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                    <span> Chapter:</span>
                </label>
                <Select
                    className="min-w-[200px]"
                    placeholder="search chapter"
                    // @ts-ignore
                    labelInValue
                    showSearch
                    filterOption={true}
                    onChange={(val) => onSelectedChapter(val)}
                    options={chapterOptions.map((item) => ({ label: item.name, value: item.id }))}
                />
            </section>


            <section className="mt-[20px]">
                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                    <span> Action:</span>
                </label>
                <Space>
                    <Select
                        value={actionVal}
                        style={{ width: 120 }}
                        onChange={(val) => setActionVal(val)}
                        options={[
                            { value: 'NONE', label: 'None' },
                            { value: 'MOVE', label: 'Move' },
                            { value: 'DELETE', label: 'Delete' },
                        ]}
                    />
                    {
                        actionVal === 'MOVE' && <Select
                            value={moveVolumeVal}
                            style={{ width: 120 }}
                            onChange={(val) => setMoveVolumeVal(val)}
                            options={[
                                { value: 'VOL', label: 'Vol' },
                            ]}
                        />
                    }
                    <Button onClick={onClickApply}>
                        Apply
                    </Button>
                </Space>
            </section>

            <section className="mt-[20px]">
                <Collapse defaultActiveKey={['1']} onChange={onChange} expandIconPosition='end'>
                    <Panel header={<>
                        <div className="flex space-x-2">
                            <Checkbox />
                            <span>No volume</span>
                        </div>
                    </>} key="1"  >

                        <Timeline
                            className="px-[20px]"
                            items={[
                                {
                                    children: 'Create a services site 2015-09-01',
                                    dot: <Checkbox />,
                                },
                                {
                                    children: 'Solve initial network problems 2015-09-01',
                                    dot: <Checkbox />,
                                },
                                {
                                    children: 'Technical testing 2015-09-01',
                                    dot: <Checkbox />,
                                },
                                {
                                    children: 'Network problems being solved 2015-09-01',
                                    dot: <Checkbox />,
                                },
                            ]}
                        />
                    </Panel>

                </Collapse>
            </section>
        </div>)
};


export default MangaChapterInfo;
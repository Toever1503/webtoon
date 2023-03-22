import { Button, Checkbox, Collapse, Select, Space, Timeline } from "antd";
import { useState } from "react";


const { Panel } = Collapse;

const MangaChapterInfo: React.FC = () => {
    const [chapterOptions, setChapterOptions] = useState(['jack', 'lucy']);

    const onChange = (key: string | string[]) => {
        console.log(key);
    };

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
    }
    return (
        <div className="px-[15px]">
            <section>
                <p className='text-[15px] text-[#1d2327] font-bold py-[10px] m-0 '>Chapter</p>
                <Select
                    className="min-w-[200px]"
                    placeholder="search chapter"
                    // @ts-ignore
                    labelInValue
                    showSearch
                    filterOption={true}
                    options={chapterOptions.map((item) => ({ label: item, value: item }))}
                />
            </section>


            <section className="mt-[20px]">
                <span className="text-base font-bold">Action: </span>
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
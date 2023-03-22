import { PlusOutlined } from "@ant-design/icons";
import { RichTextEditorComponent } from "@syncfusion/ej2-react-richtexteditor";
import { Button, Checkbox, Divider, Input, InputRef, Select, Space } from "antd";
import { useRef, useState } from "react";
import RichtextEditorForm from "../../../../components/RichtextEditorForm";

const MangaUploadSingleChapter: React.FC = () => {
    const [volumeOptions, setVolumeOptions] = useState(['jack', 'lucy']);
    const [isAddingNewVolume, setIsAddingNewVolume] = useState<boolean>(false);
    const volumeInputRef = useRef<InputRef>(null);
    const addNewVolume = (e: React.MouseEvent<HTMLButtonElement | HTMLAnchorElement>) => {
        console.log('add tag:', e.target);
    };

    const [chapterContentEditorRef, setChapterContentEditorRef] = useState<RichTextEditorComponent>();
    const onReadyMangaContentEditor = (rteObj: RichTextEditorComponent) => {
        setChapterContentEditorRef(rteObj);
    };

    return (
        <div className="px-[15px] grid gap-y-[10px]">
            <section>
                <p className='text-[15px] text-[#1d2327] font-bold py-[10px] m-0 '>Volume</p>
                <Select
                    className="min-w-[180px]"
                    placeholder="custom dropdown render"
                    // @ts-ignore
                    labelInValue
                    showSearch
                    filterOption={true}
                    dropdownRender={(menu) => (
                        <>
                            {menu}
                            <Divider style={{ margin: '8px 0' }} />
                            <Space style={{ padding: '0 8px 4px' }}>
                                <Input
                                    placeholder="Please enter item"
                                    ref={volumeInputRef}
                                />
                                <Button type="text" icon={<PlusOutlined />} loading={isAddingNewVolume} onClick={addNewVolume}>
                                    Add item
                                </Button>
                            </Space>
                        </>
                    )}
                    options={volumeOptions.map((item) => ({ label: item, value: item }))}
                />
            </section>

            <section>
                <p className='text-[15px] text-[#1d2327] font-bold py-[10px] m-0 '>Chapter name</p>
                <Input placeholder="enter your chapter name" />
            </section>

            <section>
                <p className='text-[15px] text-[#1d2327] font-bold py-[10px] m-0 '>Chapter Index(Optional)</p>
                <Input placeholder="enter your chapter name" />
            </section>
            <section className="flex items-center space-x-3">
                <p className='text-[15px] text-[#1d2327] font-bold py-[10px] m-0 '>Require vip:</p>
                <Checkbox checked />
            </section>
            <section >
                <p className='text-[15px] text-[#1d2327] font-bold py-[10px] m-0 '>Chapter content</p>
                <div className='max-w-[700px]'>
                    <RichtextEditorForm onReady={onReadyMangaContentEditor} />
                </div>
            </section>

            <Button type="primary" className="w-fit">
                Create chapter
            </Button>
        </div>)
};


export default MangaUploadSingleChapter;
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { RichTextEditorComponent } from "@syncfusion/ej2-react-richtexteditor";
import { Button, Checkbox, Divider, Input, InputRef, message, Radio, Select, Space } from "antd";
import { CheckboxChangeEvent } from "antd/es/checkbox";
import { AxiosResponse } from "axios";
import React, { useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import Sortable from "sortablejs";
import RichtextEditorForm from "../../../../components/RichtextEditorForm";
import mangaService, { MangaInput } from "../../../../services/manga/MangaService";
import { showNofification } from "../../../../stores/features/notification/notificationSlice";
import MangaUploadChapterModal from "./MangaUploadChapterModal";

type MangaUploadSingleChapterProps = {
    mangaInput: MangaInput,
}

export type MangaVolumeOptionType = {
    id: number,
    name: string,
    volumeIndex?: number,
}


const MangaUploadSingleChapter: React.FC<MangaUploadSingleChapterProps> = (props: MangaUploadSingleChapterProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();
    // begin volume select
    const [volumeOptions, setVolumeOptions] = useState<MangaVolumeOptionType[]>([
        {
            id: 0,
            name: 'Volume 1'
        }
    ]);

    const [isAddingNewVolume, setIsAddingNewVolume] = useState<boolean>(false);
    const [volumeVal, setVolumeVal] = useState<string>('');
    const addNewVolume = () => {
        console.log('add tag:', volumeVal);
        if (volumeVal) {
            setIsAddingNewVolume(true);

            mangaService.createNewVolume(
                {
                    // mangaId: props.mangaInput.id,
                    mangaId: 1,
                    name: volumeVal,
                    volumeIndex: 0
                }
            )
                .then((res) => {
                    console.log(res.data);
                    setVolumeOptions([...volumeOptions, {
                        id: res.data.id,
                        name: volumeVal
                    }]);
                    message.success(t('manga.form.success.create-volume'));
                    setVolumeVal('');
                })
                .catch((err) => {
                    console.log(err);
                    message.error(t('manga.form.errors.create-volume-failed'));
                })
                .finally(() => {
                    setIsAddingNewVolume(false);
                });
        }
        else {
            message.error(t('manga.form.errors.volume-required'));
        }

    };
    // end volume select

    const [selectedVolume, setSelectedVolume] = useState<number>(volumeOptions[0].id);


    const [showUploadChapterModal, setShowUploadChapterModal] = useState<boolean>(false);

    useEffect(() => {
        mangaService
            .filterVolume(1)
            .then((res: AxiosResponse<MangaVolumeOptionType[]>) => {
                setVolumeOptions(res.data);
            })
            .catch((err) => {
                console.log('get volume error:', err);
            });



    }, []);

    return (
        <div className="px-[15px] grid gap-y-[10px]">
            <section>
                <label className='text-[16px] font-bold mb-[5px] flex items-center gap-[2px] mt-[10px]'>
                    <span> Volume:</span>
                </label>
                <Select
                    className="min-w-[250px]"
                    placeholder="custom dropdown render"
                    // @ts-ignore
                    labelInValue
                    showSearch
                    value={selectedVolume}
                    filterOption={true}
                    onChange={(value) => setSelectedVolume(value)}
                    dropdownRender={(menu) => (
                        <>
                            {menu}
                            <Divider style={{ margin: '8px 0' }} />
                            <Space style={{ padding: '0 8px 4px' }}>
                                <Input
                                    placeholder="Please enter item"
                                    value={volumeVal}
                                    onChange={(e) => setVolumeVal(e.target.value)}
                                />
                                <Button type="text" icon={<PlusOutlined />} loading={isAddingNewVolume} onClick={addNewVolume}>
                                    Add item
                                </Button>
                            </Space>
                        </>
                    )}
                    options={volumeOptions.map((item) => ({ label: item.name, value: item.id }))}
                />
            </section>
            <Button onClick={() => setShowUploadChapterModal(true)}>Add new chapter</Button>
            <MangaUploadChapterModal visible={showUploadChapterModal} onOk={() => { }} onCancel={() => { setShowUploadChapterModal(false) }} title={'fasf'} mangaInput={props.mangaInput} />
        </div>)


};


export default MangaUploadSingleChapter;
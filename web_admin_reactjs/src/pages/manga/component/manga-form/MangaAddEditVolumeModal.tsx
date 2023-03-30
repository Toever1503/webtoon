import { Button, Input, message, Modal, Space } from "antd";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import mangaService, { MangaInput } from "../../../../services/manga/MangaService";


type MangaAddEditVolumeModalProps = {
    visible: boolean;
    title: string;
    onOk: (volume: VolumeForm) => void;
    onCancel: (...args: Object[]) => void;
    mangaInput: MangaInput,
    volumeInput?: VolumeForm,
}

export type VolumeForm = {
    id: number | string,
    name: string,
    volumeIndex: number,
    mangaId?: number | string,
}

const MangaAddEditVolumeModal: React.FC<MangaAddEditVolumeModalProps> = (props: MangaAddEditVolumeModalProps) => {
    const { t } = useTranslation();

    const [volumeVal, setVolumeVal] = useState<string>('');

    const [isSubmitingForm, setIsSubmitingForm] = useState(false);

    const onSubmitForm = () => {

        console.log('add tag:', volumeVal);
        if (isSubmitingForm) return;
        if (volumeVal) {
            setIsSubmitingForm(true);

            if (!props.volumeInput)
                mangaService.createNewVolume(
                    {
                        mangaId: props.mangaInput.id,
                        name: volumeVal,
                        volumeIndex: 0
                    }
                )
                    .then((res) => {
                        console.log(res.data);
                        props.onOk({
                            id: res.data.id,
                            name: volumeVal,
                            volumeIndex: res.data.volumeIndex,
                        })
                        message.success(t('manga.form.volume.errors.add-success'));
                        setVolumeVal('');
                    })
                    .catch((err) => {
                        console.log(err);
                        message.error(t('manga.form.volume.errors.add-failed'));
                    })
                    .finally(() => {
                        setIsSubmitingForm(false);
                    });
            else mangaService.updateVolume(
                props.volumeInput.id,
                {
                    id: 0,
                    mangaId: props.mangaInput.id,
                    name: volumeVal,
                    volumeIndex: 0
                }
            )
                .then((res) => {
                    console.log(res.data);
                    props.onOk({
                        id: res.data.id,
                        name: volumeVal,
                        volumeIndex: res.data.volumeIndex,
                    })
                    message.success(t('manga.form.volume.errors.edit-success'));
                    setVolumeVal('');
                })
                .catch((err) => {
                    console.log(err);
                    message.error(t('manga.form.volume.errors.edit-failed'));
                })
                .finally(() => {
                    setIsSubmitingForm(false);
                });
        }
        else {
            message.error(t('manga.form.errors.volume-required'));
        }

    }

    useEffect(() => {
        if (props.volumeInput) {
            setVolumeVal(props.volumeInput.name);
        }
        else
            setVolumeVal('');
    }, [props.visible])
    return <>

        <Modal title={<h3 className="text-2xl">{props.title}</h3>}
            open={props.visible}
            onCancel={() => props.onCancel()}
            footer={null}
        >
            <div className="">
                <label className="text-base">{t('manga.form.volume.volume-name')}</label>
                <Input value={volumeVal} onChange={(e) => setVolumeVal(e.target.value)} placeholder={t('manga.form.volume.volume-name-placeholder') || ''} />
            </div>

            <Space className="py-[10px]">
                <Button loading={isSubmitingForm} onClick={onSubmitForm}>
                    {t('manga.form.volume.save-volume-btn')}
                </Button>
                <Button onClick={props.onCancel}>
                    {t('manga.form.volume.cancel-volume-btn')}
                </Button>
            </Space>
        </Modal>
    </>
};

export default MangaAddEditVolumeModal;
import { useEffect, useState } from "react";
import { Button, Divider, Input, Select, Space } from "antd";
import debounce from "../../../../utils/debounce";
import { PlusOutlined } from "@ant-design/icons";
import { MangaInput } from "../../../../services/manga/MangaService";
import { MangaInputError } from "../../AddEditMangaForm";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import { AxiosResponse } from "axios";
import { showNofification } from "../../../../stores/features/notification/notificationSlice";
import tagService, { TagInput } from "../../../../services/TagService";



interface TagSelectProps {
    mangaInput: MangaInput;
    mangaInputError: MangaInputError;

}

const TagSelect: React.FC<TagSelectProps> = ({ mangaInput, mangaInputError }: TagSelectProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    // begin tag search
    // @ts-ignore
    const [selectOptions, setSelectOptions] = useState<TagInput[]>(mangaInput.originalTags ? mangaInput.originalTags : []);

    const [selectedOptions, setSelectedOptions] = useState<string[]>([]);
    const [inputVal, setInputVal] = useState('');
    const [isAddingNew, setIsAddingNew] = useState<boolean>(false);
    const onSearch = debounce((val: string) => {
        setInputVal(val);
        console.log('search tag: ', val);
        onCallApiSearch(val, 'tagName,asc');
    });
    const onCallApiSearch = (s: string = '', sort: string = 'id,desc') => {
        tagService.filterTag({
            s,
            page: 0,
            size: 10,
            sort: sort,
        })
            .then((res) => {
                console.log('seach Tag res', res.data.content);
                setSelectOptions(res.data.content);
            })
    };

    const addTag = () => {
        if (isAddingNew) return;
        if (inputVal) {
            setIsAddingNew(true);
            tagService.addTag({
                tagName: inputVal,
            })
                .then((res: AxiosResponse<TagInput>) => {
                    console.log('add ok: ', res.data);
                    setSelectOptions([res.data, ...selectOptions]);
                    dispatch(showNofification({
                        type: 'success',
                        message: 'Thêm thẻ thành công!',
                    }));
                    setInputVal('');
                })
                .catch((err) => {
                    console.log('add err: ', err);
                    dispatch(showNofification({
                        type: 'error',
                        message: 'Thêm thẻ thất bại!',
                    }));
                })
                .finally(() => {
                    setIsAddingNew(false);
                });
        }
        else {
            dispatch(showNofification({
                type: 'error',
                message: 'Vui lòng nhập tên thẻ bạn muốn thêm!',
            }));
        }
    }
    // end search

    useEffect(() => {
        onCallApiSearch();
        // @ts-ignore
        setSelectedOptions(mangaInput.originalTags ? mangaInput.originalTags.map((item: any) => item.id?.toString()) : []);
    }, []);
    return (
        <div className='grid gap-y-[5px] px-[10px]'>
            <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                <span> Thẻ:</span>
            </label>
            <Select
                className='w-full'
                mode="multiple"
                placeholder="chọn thẻ"
                // @ts-ignore
                onSearch={onSearch}
                labelInValue
                showSearch
                allowClear
                filterOption={false}
                dropdownRender={(menu) => (
                    <>
                        {menu}
                        <Divider style={{ margin: '8px 0' }} />
                        <Space style={{ padding: '0 8px 4px' }}>
                            <Input
                                className=""
                                placeholder="nhập tên"
                                value={inputVal}
                                onChange={val => setInputVal(val.target.value)}
                            />
                            <Button type="text" icon={<PlusOutlined />} loading={isAddingNew} onClick={addTag}>
                                Thêm mới
                            </Button>
                        </Space>
                    </>
                )}
                value={selectedOptions}
                onChange={(val: any) => {
                    console.log('change: ', val);
                    setSelectedOptions(val);
                    if (typeof val === 'object')
                        mangaInput.tags = val.map((item: any) => item.value);
                    else
                        mangaInput.tags = val.map((item: any) => item);
                }}
                options={selectOptions ? selectOptions.map((item: TagInput) => ({ label: item.tagName, value: item.id?.toString() })) : []}
            />


        </div>
    )
};
export default TagSelect;
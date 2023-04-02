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
    const [selectOptions, setSelectOptions] = useState<TagInput[]>([]);
    const [selectedOptions, setSelectedOptions] = useState<string[]>(mangaInput.tags);
    const [inputVal, setInputVal] = useState('');
    const [isAddingNew, setIsAddingNew] = useState<boolean>(false);
    const onSearch = debounce((val: string) => {
        setInputVal(val);
        console.log('search tag: ', val);
        onCallApiSearch(val, 'name,asc');
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
        setIsAddingNew(true);
        if (inputVal)
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
    // end search

    useEffect(() => {
        onCallApiSearch();
         // @ts-ignore
         setSelectOptions(mangaInput.tags);
    }, []);
    return (
        <div className='grid gap-y-[5px] px-[10px]'>
            <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                <span className='text-red-500'>*</span>
                <span> Tags:</span>
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
                onChange={(val: string[]) => {
                    console.log('change: ', val);
                    setSelectedOptions(val);
                    mangaInput.tags = val.map((item: any) => item.value);
                }}
                options={selectOptions ? selectOptions.map((item: TagInput) => ({ label: item.tagName, value: item.id?.toString() })) : []}
            />

            {
                mangaInputError.tags &&
                <p className='text-[12px] text-red-500 px-[5px]'>
                    {t(mangaInputError.tags)}
                </p>
            }
        </div>
    )
};
export default TagSelect;
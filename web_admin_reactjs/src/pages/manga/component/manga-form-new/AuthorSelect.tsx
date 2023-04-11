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
import authorService from "../../../../services/manga/AuthorService";
import { AuthorInput } from "../../../../services/manga/AuthorService";



interface AuthorSelectProps {
    mangaInput: MangaInput;
    mangaInputError: MangaInputError;

}

const AuthorSelect: React.FC<AuthorSelectProps> = ({ mangaInput, mangaInputError }: AuthorSelectProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    // begin tag search
    // @ts-ignore
    const [selectOptions, setSelectOptions] = useState<AuthorInput[]>(mangaInput.originalAuthors ? mangaInput.originalAuthors : []);
    const [selectedOptions, setSelectedOptions] = useState<string[]>([]);
    const [inputVal, setInputVal] = useState('');
    const [isAddingNew, setIsAddingNew] = useState<boolean>(false);
    const onSearch = debounce((val: string) => {
        setInputVal(val);
        console.log('search tag: ', val);
        onCallApiSearch(val, 'name,asc');
    });
    const onCallApiSearch = (s: string = '', sort: string = 'id,desc') => {
        authorService.filterAuthor({
            s,
            page: 0,
            size: 10,
            sort: sort,
        })
            .then((res) => {
                console.log('seach author res', res.data.content);
                setSelectOptions(res.data.content);
            })
    };

    const addAuthor = () => {
        if (isAddingNew) return;
        if (inputVal) {
            setIsAddingNew(true);
            authorService.addAuthor({
                name: inputVal,
            })
                .then((res: AxiosResponse<AuthorInput>) => {
                    console.log('add ok: ', res.data);
                    setSelectOptions([res.data, ...selectOptions]);
                    dispatch(showNofification({
                        type: 'success',
                        message: 'Thêm tác giả thành công!',
                    }));
                    setInputVal('');
                })
                .catch((err) => {
                    console.log('add err: ', err);
                    dispatch(showNofification({
                        type: 'error',
                        message: 'Thêm tác giả thất bại!',
                    }));
                })
                .finally(() => {
                    setIsAddingNew(false);
                });
        }
        else {
            dispatch(showNofification({
                type: 'error',
                message: 'Vui lòng nhập tên tác giả bạn muốn thêm!',
            }));
        }
    }
    // end search

    useEffect(() => {
        onCallApiSearch();
        // @ts-ignore
        setSelectedOptions(mangaInput.originalAuthors ? mangaInput.originalAuthors.map((item: AuthorInput) => item.id?.toString()) : []);
    }, []);
    return (
        <div className='grid gap-y-[5px] px-[10px]'>
            <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                <span> Tác giả:</span>
            </label>
            <Select
                className='w-full'
                mode="multiple"
                placeholder="chọn tác giả"
                // @ts-ignore
                onSearch={onSearch}
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
                            <Button type="text" icon={<PlusOutlined />} loading={isAddingNew} onClick={addAuthor}>
                                Thêm mới
                            </Button>
                        </Space>
                    </>
                )}
                value={selectedOptions}
                onChange={(val) => {
                    console.log('change: ', val);
                    setSelectedOptions(val);
                    mangaInput.authors = val.map((item: any) => item);
                }}
                options={selectOptions ? selectOptions.map((item: AuthorInput) => ({ label: item.name, value: item.id?.toString() })) : []}
            />
        </div>
    )
};
export default AuthorSelect;
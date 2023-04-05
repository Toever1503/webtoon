import { useEffect, useRef, useState } from "react";
import genreService, { GenreInput } from "../../../../services/manga/GenreService";
import { Button, Divider, Input, InputRef, Select, Space } from "antd";
import debounce from "../../../../utils/debounce";
import { PlusOutlined } from "@ant-design/icons";
import { MangaInput } from "../../../../services/manga/MangaService";
import { MangaInputError } from "../../AddEditMangaForm";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import { AxiosResponse } from "axios";
import { showNofification } from "../../../../stores/features/notification/notificationSlice";



interface GenreSelectProps {
    mangaInput: MangaInput;
    mangaInputError: MangaInputError;

}

const GenreSelect: React.FC<GenreSelectProps> = ({ mangaInput, mangaInputError }: GenreSelectProps) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    // begin tag search
    // @ts-ignore
    const [selectOptions, setSelectOptions] = useState<GenreInput[]>(mangaInput.originalGenres ? mangaInput.originalGenres : []);
    const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
    const [genreSearchVal, setGenreSearchVal] = useState('');
    const [isAddingNewGenre, setIsAddingNewGenre] = useState<boolean>(false);
    const onSearchGenre = debounce((val: string) => {
        console.log('search tag: ', val);
        setGenreSearchVal(val);
        onCallApiSearchGenre(val, 'name,asc');
    });
    const onCallApiSearchGenre = (s: string = '', sort: string = 'id,desc') => {
        genreService.filterGenre({
            s,
            page: 0,
            size: 10,
            sort: sort,
        })
            .then((res) => {
                console.log('seach genre res', res.data.content);
                setSelectOptions(res.data.content);
            })
    };

    const addGenre = () => {
        if (isAddingNewGenre) return;
        if (genreSearchVal) {
            setIsAddingNewGenre(true);
            genreService.addGenre({
                name: genreSearchVal,
            })
                .then((res: AxiosResponse<GenreInput>) => {
                    console.log('add ok: ', res.data);
                    setSelectOptions([res.data, ...selectOptions]);
                    dispatch(showNofification({
                        type: 'success',
                        message: 'Add genre success',
                    }));
                    setGenreSearchVal('');
                })
                .catch((err) => {
                    console.log('add err: ', err);
                    dispatch(showNofification({
                        type: 'error',
                        message: 'Add genre failed',
                    }));
                })
                .finally(() => {
                    setIsAddingNewGenre(false);
                });
        }
        else {
            dispatch(showNofification({
                type: 'error',
                message: 'Vui lòng nhập tên thể loại bạn muốn thêm!',
            }));
        }
    }
    // end tag search

    useEffect(() => {
        // let id: number | undefined;
        // id = setInterval(() => {
        //     autoSaveMangaInfo(mangaInput);
        // }, 15000);
        // return () => clearInterval(id);
        onCallApiSearchGenre();
        // @ts-ignore
        setSelectedGenres(mangaInput.originalGenres ? mangaInput.originalGenres.map((item: any) => item.id?.toString()) : []);

    }, []);
    return (
        <div className='grid gap-y-[5px] px-[10px]'>
            <label className='text-[14px] font-bold mb-[5px] flex items-center gap-[2px]'>
                <span> Thể loại:</span>
            </label>
            <Select
                className='w-full'
                mode="multiple"
                placeholder="chọn thể loại"
                // @ts-ignore
                onSearch={onSearchGenre}
                showSearch
                allowClear
                filterOption={false}
                dropdownRender={(menu) => (
                    <>
                        {menu}
                        <Divider style={{ margin: '8px 0' }} />
                        <Space style={{ padding: '0 8px 4px' }}>
                            <Input
                            className=''
                                placeholder="nhập tên"
                                value={genreSearchVal}
                                onChange={val => setGenreSearchVal(val.target.value)}
                            />
                            <Button type="text" icon={<PlusOutlined />} loading={isAddingNewGenre} onClick={addGenre}>
                                Thêm mới
                            </Button>
                        </Space>
                    </>
                )}
                value={selectedGenres}
                onChange={(val: string[]) => {
                    console.log('change: ', val);
                    setSelectedGenres(val);
                    mangaInput.genres = val.map((item: any) => item);
                }}
                options={selectOptions ? selectOptions.map((item: GenreInput) => ({ label: item.name, value: item.id?.toString() })) : []}
            />


        </div>
    )
};
export default GenreSelect;
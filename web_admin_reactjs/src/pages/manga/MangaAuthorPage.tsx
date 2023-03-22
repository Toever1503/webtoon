import React from 'react';
import { Button, Space, Table, TablePaginationConfig, Tag, Input } from 'antd';
import type { ColumnsType } from 'antd/es/table';

interface DataType {
    key: string | number;
    name: string;
    age: number;
    address: string;
    tags: string[];
    stt: string | number;
}

const { Search } = Input;

const columns: ColumnsType<DataType> = [
    {
        title: 'STT',
        dataIndex: 'stt',
        key: 'stt',
    },
    {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        render: (text) => <a>{text}</a>,
    },
    {
        title: 'Age',
        dataIndex: 'age',
        key: 'age',
    },
    {
        title: 'Address',
        dataIndex: 'address',
        key: 'address',
    },
    {
        title: 'Tags',
        key: 'tags',
        dataIndex: 'tags',
        render: (_, { tags }) => (
            <>
                {tags.map((tag) => {
                    let color = tag.length > 5 ? 'geekblue' : 'green';
                    if (tag === 'loser') {
                        color = 'volcano';
                    }
                    return (
                        <Tag color={color} key={tag}>
                            {tag.toUpperCase()}
                        </Tag>
                    );
                })}
            </>
        ),
    },
    {
        title: 'Action',
        key: 'action',
        render: (_, record) => (
            <Space size="middle">
                <a>Invite {record.name}</a>
                <a>Delete</a>
            </Space>
        ),
    },
];

const data: DataType[] = [];
for (let i = 1; i < 100; ++i) {
    data.push(
        {
            stt: i,
            key: i.toString(),
            name: 'John Brown',
            age: 32,
            address: 'New York No. 1 Lake Park',
            tags: ['nice', 'developer'],
        },
    )
}

const pagination = (page: TablePaginationConfig) => {
    console.log('page', page);
}

const onSearch = (value: string) => console.log(value);

const MangaAuthorPage: React.FC = () =>
    <div className="space-y-3">
        <div className="flex space-x-3">
            <p className="text-[23px] font-[400]">Manga</p>
            <Button className="font-medium">Add new</Button>
        </div>
        <div className="flex justify-between">
            <div className="flex space-x-3 items-center">
                <div className="flex space-x-[2px]">
                    <p className="m-0">All</p><p className="m-0">(2)</p>
                </div>
                <div>
                    <p className="m-0">|</p>
                </div>
                <div className="flex space-x-[2px]">
                    <p className="m-0">Published</p><p className="m-0">(2)</p>
                </div>
            </div>
            <div>
                <Search placeholder="input search text" onSearch={onSearch} style={{ width: 200 }} />
            </div>
        </div>
        <Table columns={columns} dataSource={data} onChange={pagination} />
    </div>

export default MangaAuthorPage;
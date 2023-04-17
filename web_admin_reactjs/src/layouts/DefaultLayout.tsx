import React, { useState } from 'react';
import {
    TeamOutlined,
    UserOutlined,
} from '@ant-design/icons';
import { MenuProps, notification } from 'antd';
import { Layout, Menu, theme } from 'antd';
import { Outlet, useNavigate } from 'react-router-dom';
import { AppstoreOutlined, AreaChartOutlined, CommentOutlined, DashboardOutlined, FormOutlined, ShoppingCartOutlined, TagsOutlined } from '@ant-design/icons/lib/icons';
import NotificationComponent from '../components/NotificationComponent';

const { Header, Content, Footer, Sider } = Layout;


type MenuItem = Required<MenuProps>['items'][number];

function getItem(
    label: React.ReactNode,
    key: React.Key,
    icon?: React.ReactNode,
    children?: MenuItem[],
): MenuItem {
    return {
        key,
        icon,
        children,
        label,
    } as MenuItem;
}

const items: MenuItem[] = [
    getItem('Bảng điều khiển', '/', <DashboardOutlined />),
    getItem('Đơn hàng', '/orders', <ShoppingCartOutlined />),
    getItem('Thống kê', '/stats', <AreaChartOutlined />),
    getItem('Gói đọc', '/subscription-packs', <AppstoreOutlined />),
    getItem('Manga', 'parent-mangas',<FormOutlined />, [
        getItem('All mangas', '/mangas'),
        getItem('Manga genres', '/mangas/genres'),
        getItem('Manga authors', '/mangas/authors')
    ]),
    getItem('Tin tức', 'parent-posts', <FormOutlined />, [
        getItem('All Posts', 'posts'),
        getItem('Categories', 'categories')
    ]),
    getItem('Thẻ', '/tag', <TagsOutlined />),
    getItem('Bình luận', 'comments', <CommentOutlined />),
    getItem('Người dùng', 'parent-users', <UserOutlined />, [
        getItem('All Users', 'users'),
        getItem('My Profile', 'user-profile'),
    ]),
];



const onSubMenuClick = (item: MenuItem) => {
    console.log('onSubMenuClick', item);
}

const App: React.FC = () => {
    const navigate = useNavigate();
    const [collapsed, setCollapsed] = useState(false);
    const {
        token: { colorBgContainer },
    } = theme.useToken();

    const onMenuClick = (item: MenuItem) => {
        console.log('onMenuClick', item?.key);
        if (item)
            if (item.key)
                navigate(item.key.toString());
    }

    return (
        <>
            <NotificationComponent />
            <Layout style={{ minHeight: '100vh' }}>

                <Sider theme="light" collapsible collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
                    <div className='flex items-center justify-center' style={{ height: 64, boxShadow: '0 1px 9px -3px rgba(0,0,0,.2)', zIndex: 1, position: 'relative' }} >
                        <span className='font-bold text-2xl'>Admin</span>
                    </div>
                    <Menu onSelect={onMenuClick} triggerSubMenuAction='click' defaultSelectedKeys={['1']} mode="inline" items={items} />
                </Sider>

                <Layout className="site-layout">

                    <Header style={{ padding: 0, background: colorBgContainer }} >
                        fsafs
                    </Header>

                    <Content style={{ margin: '0 16px' }}>
                        <Outlet />
                    </Content>



                    <Footer style={{ textAlign: 'center' }}>Ant Design ©2023 Created by Ant UED</Footer>
                </Layout>
            </Layout>
        </>
    );
};

export default App;
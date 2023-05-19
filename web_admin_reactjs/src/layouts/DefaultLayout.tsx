import React, { useEffect, useState } from 'react';
import {
    LogoutOutlined,
    UserOutlined,
} from '@ant-design/icons';
import { Divider, MenuProps, notification } from 'antd';
import { Layout, Menu, theme } from 'antd';
import { Outlet, useNavigate } from 'react-router-dom';
import { AppstoreOutlined, AreaChartOutlined, CommentOutlined, DashboardOutlined, FormOutlined, ShoppingCartOutlined, TagsOutlined } from '@ant-design/icons/lib/icons';
import NotificationComponent from '../components/NotificationComponent';
import { eraseCookie, getCookie, hasAnyAuths } from '../plugins/cookieUtil';
import { showNofification } from '../stores/features/notification/notificationSlice';
import { useTranslation } from 'react-i18next';
import { useDispatch } from 'react-redux';

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





const onSubMenuClick = (item: MenuItem) => {
    console.log('onSubMenuClick', item);
}

const App: React.FC = () => {
    const { t } = useTranslation();
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [collapsed, setCollapsed] = useState(false);
    const {
        token: { colorBgContainer },
    } = theme.useToken();

    const [menuItems, setMenuItems] = useState<MenuItem[]>([
        getItem('Bảng điều khiển', '/', <DashboardOutlined />)
    ]);

    const onMenuClick = (item: MenuItem) => {
        console.log('onMenuClick', item?.key);
        if (item)
            if (item.key)
                navigate(item.key.toString());
    }

    const logout = () => {
        console.log('logout');

        eraseCookie('token');
        setTimeout(() => {

            dispatch(showNofification({
                type: 'success',
                message: t('login.logout-success'),
            }))
            navigate('/signin');
        }, 200);
    }

    useEffect(() => {
        if (!hasAnyAuths(['ADMIN', 'EMP'])) {
            dispatch(showNofification({
                type: 'error',
                message: 'Bạn không có quyền truy cập trang này!'
            }));
            navigate('/signin');
        }
        getCookie('token') ? null : navigate('/signin');


        let mMenu: MenuItem[] = [getItem('Bảng điều khiển', '/', <DashboardOutlined />)];

        if (hasAnyAuths(['ROLE_VIEW_STATISTIC']))
            mMenu.push(getItem('Thống kê', '/stats/revenue', <AreaChartOutlined />));
            
        if (hasAnyAuths(['ROLE_MANAGE_ORDER']))
            mMenu.push(getItem('Quản lý đơn hàng', 'orders', <ShoppingCartOutlined />, [
                getItem('Danh sách đơn hàng', '/orders'),
                getItem('Trạng thái đăng ký', '/stats/registration-status'),
            ]));

        if (hasAnyAuths(['ADMIN']))
            mMenu.push(getItem('Quản lý gói đọc', '/subscription-packs', <AppstoreOutlined />,));

        if (hasAnyAuths(['ROLE_MANAGE_MANGA']))
            mMenu.push(getItem('Quản lý truyện', 'parent-mangas', <FormOutlined />, [
                getItem('Tất cả truyện', '/mangas'),
                getItem('Quản lý thể loại', '/mangas/genres'),
                getItem('Quản lý tác giả', '/mangas/authors'),
                getItem('Thẻ', '/tag')
            ]),)
        if (hasAnyAuths(['ADMIN'])) {
            mMenu.push(getItem('Người dùng', 'parent-users', <UserOutlined />, [
                getItem('Danh sách người dùng', 'users'),
                // getItem('My Profile', 'user-profile'),
            ]));
        }
        // [
        //     getItem('Bảng điều khiển', '/', <DashboardOutlined />),
        // getItem('Thống kê', '/stats', <AreaChartOutlined />, [
        //     getItem('Doanh thu', '/stats/revenue'),
        //     // getItem('Tỉ lệ đăng ký gói', '/stats/registration-rate'),
        //     getItem('Trạng thái đăng ký', '/stats/registration-status'),
        // ]),
        //     getItem('Quản lý gói đọc', '/subscription-packs', <AppstoreOutlined />),
        //     getItem('Quản lý truyện', 'parent-mangas', <FormOutlined />, [
        //         getItem('Tất cả truyện', '/mangas'),
        //         getItem('Quản lý thể loại', '/mangas/genres'),
        //         getItem('Quản lý tác giả', '/mangas/authors'),
        //         getItem('Thẻ', '/tag')
        //     ]),

        //     // getItem('Tin tức', 'parent-posts', <FormOutlined />, [
        //     //     getItem('All Posts', 'posts'),
        //     //     getItem('Categories', 'categories')
        //     // ]),
        //     // getItem('Bình luận', 'comments', <CommentOutlined />),
        //     getItem('Người dùng', 'parent-users', <UserOutlined />, [
        //         getItem('Quản lý người dùng', 'users'),
        //         // getItem('My Profile', 'user-profile'),
        //     ]),
        // ]
        setMenuItems(mMenu);
    }, []);

    return (
        <>
            <NotificationComponent />
            <Layout style={{ minHeight: '100vh' }}>

                <Sider theme="light" collapsible width={'220px'} collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
                    <div className='flex items-center justify-center' style={{ height: 64, boxShadow: '0 1px 9px -3px rgba(0,0,0,.2)', zIndex: 1, position: 'relative' }} >
                        <span className='font-bold text-base'>
                            {
                                localStorage.getItem('fullName')
                            }
                        </span>
                    </div>
                    <Menu onSelect={onMenuClick} triggerSubMenuAction='click' defaultSelectedKeys={['1']} mode="inline" items={menuItems} />

                    <Divider />
                    <a className='flex items-center justify-center space-x-2' onClick={logout}>
                        <span>Đăng xuất</span>
                        <LogoutOutlined />
                    </a>
                </Sider>

                <Layout className="site-layout">

                    <Header style={{ padding: 0, background: colorBgContainer }} >
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
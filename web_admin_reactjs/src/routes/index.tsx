import { createBrowserRouter } from "react-router-dom";
import DefaultLayout from "../layouts/DefaultLayout";
import CounterPage from "../pages/counter/CounterPage";
import MangaPage from "../pages/manga/MangaPage";
import AddEditMangaForm from "../pages/manga/AddEditMangaForm";
import MangaGenrePage from "../pages/manga/MangaGenrePage";
import TagPage from "../pages/tag/TagPage";
import DragPage from "../pages/test/DragPage";
import UnzipFile from "../pages/test/UnzipFile";
import MangaAuthorPage from "../pages/manga/MangaAuthorPage";
import ErrorPage from "../pages/ErrorPage";
import Dashboard from "../pages/dashboard/Dashboard";
import CategoryPage from "../pages/post/CategoryPage";
import OrderPage from "../pages/order/OrderPage";
import StatisticsPage from "../pages/stats/StatisticsPage";
import CommentPage from "../pages/comment/CommentPage";
import UserPage from "../pages/user/UserPage";
import SubscriptionPackPage from "../pages/subscription-pack/SubscriptionPackPage";
import LoginPage from "../pages/login/LoginPage";

const router = createBrowserRouter([
    {
        path: "/",
        element: <DefaultLayout />,
        children: [
            {
                path: "/",
                element: <Dashboard />,
            },
            {
                path: "/orders",
                element: <OrderPage />,
            },
            {
                path: "/subscription-packs",
                element: <SubscriptionPackPage />,
            },
            {
                path: "/mangas",
                element: <MangaPage />,
            },
            {
                path: "/mangas/add",
                element: <AddEditMangaForm type={"ADD"} />
            },
            {
                path: "/mangas/edit/:id",
                element: <AddEditMangaForm type={"EDIT"} />
            },
            {
                path: "/mangas/genres",
                element: <MangaGenrePage />
            },
            {
                path: "/mangas/authors",
                element: <MangaAuthorPage />
            },
            {
                path: "/tag",
                element: <TagPage />
            },
            {
                path: "/categories",
                element: <CategoryPage />
            },
            {
                path: "/comments",
                element: <CommentPage />
            },
            {
                path: "/stats",
                element: <StatisticsPage />
            },
            {
                path: "/users",
                element: <UserPage />
            },
            {
                path: '/ERROR',
                element:  <ErrorPage />
            }
        ],
    },
    {
        path: "/counter",
        element: <CounterPage />
    },
    {
        path: "/signin",
        element: <LoginPage />
    },
    {
        path: "/uploadzip",
        element: <UnzipFile />
    },
    
]);

export default router;
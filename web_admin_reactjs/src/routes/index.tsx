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
                path: "/contacts",
                element: <CounterPage />,
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
        path: "/drag",
        element: <DragPage />
    },
    {
        path: "/uploadzip",
        element: <UnzipFile />
    },
    
]);

export default router;
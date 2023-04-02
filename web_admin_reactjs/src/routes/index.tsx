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

const router = createBrowserRouter([
    {
        path: "/",
        element: <DefaultLayout />,
        children: [
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
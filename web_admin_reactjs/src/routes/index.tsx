import {createBrowserRouter} from "react-router-dom";
import DefaultLayout from "../layouts/DefaultLayout";
import CounterPage from "../pages/counter/CounterPage";
import MangaPage from "../pages/manga/MangaPage";
import AddNewManga from "../pages/manga/AddNewManga";
import MangaGenrePage from "../pages/manga/MangaGenrePage";
import TagPage from "../pages/tag/TagPage";
import DragPage from "../pages/test/DragPage";
import UnzipFile from "../pages/test/UnzipFile";
import MangaAuthorPage from "../pages/manga/MangaAuthorPage";

const router = createBrowserRouter([
    {
        path: "/",
        element: <DefaultLayout/>,
        children: [
            {
                path: "/contacts",
                element: <CounterPage/>,
            },
            {
                path: "/manga",
                element: <MangaPage/>,
            },
            {
                path: "/manga/add",
                element: <AddNewManga/>
            },
            {
                path: "/manga/genre",
                element: <MangaGenrePage />
            },
            {
                path: "/manga/author",
                element: <MangaAuthorPage />
            },
            {
                path: "/tag",
                element: <TagPage />
            },
        ],
    },
    {
        path: "/counter",
        element: <CounterPage/>
    },
    {
        path: "/drag",
        element: <DragPage />
    },
    {
        path: "/uploadzip",
        element: <UnzipFile />
    }
]);

export default router;
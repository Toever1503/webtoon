import {createBrowserRouter} from "react-router-dom";
import DefaultLayout from "../layouts/DefaultLayout";
import CounterPage from "../pages/counter/CounterPage";
import MangaPage from "../pages/manga/MangaPage";
import AddNewManga from "../pages/manga/component/AddNewManga";

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
        ],
    },
    {
        path: "/counter",
        element: <CounterPage/>
    }
]);

export default router;
import React from 'react'
import ReactDOM from 'react-dom/client'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import RouteLayout from "./routes/RouteLayout.tsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <RouteLayout/>,
        children: [{}]
    }
])

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>,
)

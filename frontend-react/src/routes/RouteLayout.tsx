import MainHeader from "../components/header/MainHeader.tsx";
import Hero from "../components/hero/Hero.tsx";
import {Outlet} from "react-router-dom";
import Model from "../components/model/Model.tsx";
import Login from "./login/Login.tsx";
import {useState} from "react";

function RouteLayout() {
    const [loginModelVisible, setLoginModelVisible] = useState(false);

    const showLoginModel = () => {
        setLoginModelVisible(true)
    }

    const hiddeLoginModel = () => {
        setLoginModelVisible(false)
    }

    return (
        <>
            {loginModelVisible && <Model>
                <Login onClose={hiddeLoginModel}/>
            </Model>}
            <MainHeader onOpenLogin={showLoginModel}/>
            <Hero/>
            <Outlet/>
        </>
    )
}

export default RouteLayout
import classes from "./MainHeader.module.css";
import {Link} from "react-router-dom";
import {MouseEventHandler} from "react";

type Props = {
    onOpenLogin: MouseEventHandler<HTMLElement> | undefined;
}

function MainHeader({onOpenLogin}: Props) {
    return (
        <>
            <div className={classes.navbar}>
                <div className={classes.logo}>
                    <Link to="/">
                        <p>TODO</p>
                    </Link>

                </div>
                <div className={classes.navlink}>
                    <ul>
                        <li><Link to="/" onClick={onOpenLogin}><p>Login</p></Link></li>
                        <li><Link to="/"><p>Signup</p></Link></li>
                    </ul>
                </div>
            </div>

        </>
    )
}

export default MainHeader
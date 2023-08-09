import classes from "./MainHeader.module.css";

function MainHeader() {
    return (
        <>
            {/*main header*/}
            <div className={classes.navbar}>
                <div className={classes.logo}>
                    <a href="#">TODO</a>
                </div>
                <div className={classes.navlink}>
                    <ul>
                        <li><a href="#">Login</a></li>
                        <li><a href="#">Signup</a></li>
                    </ul>
                </div>
            </div>

        </>
    )
}

export default MainHeader
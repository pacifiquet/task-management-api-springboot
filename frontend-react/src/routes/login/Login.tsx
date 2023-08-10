import classes from './Login.module.css'
import google from '../../assets/google-icongoogle.png';
import facebook from '../../assets/facebookfacebook.png';
import closeIcon from '../../assets/211652_close_icon.svg'
import {MouseEventHandler} from "react";

type Props = {
    onClose: MouseEventHandler<HTMLElement> | undefined;
}

function Login({onClose}: Props) {
    return (
        <form className={classes.loginForm}>
            <div className={classes.formCLose}>
                <img src={closeIcon} alt="closeIcon" onClick={onClose}/>

            </div>
            <div className={classes.formHeader}>
                <h1>Login</h1>
            </div>
            <p>
                <img src={google} alt="google"/>
                <img src={facebook} alt="facbook"/>
            </p>
            <p><strong>Or</strong></p>
            <div className={classes.inputFields}>
                <input type="email" id="email" placeholder="email"/>
                <input type="password" id="password" placeholder="password"/>
            </div>

            <button type="submit">Login</button>
            <div className={classes.actions}>
                <p>Forgot the password?</p>
                <a href="#">Reset</a>
            </div>

        </form>
    )
}

export default Login
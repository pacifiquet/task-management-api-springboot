import classes from "./Hero.module.css";

function HeroSection() {

    return (
        <div className={classes.heroSection}>
            <div>
                <h1>have you tried <strong>TODO</strong> yet?</h1>
            </div>
            <div>
                <button className={classes.heroButton}>Sign up</button>
            </div>
        </div>
    )
}

export default HeroSection
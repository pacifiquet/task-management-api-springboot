import classes from './Model.module.css'
import {ReactNode} from "react";

type Props = {
    children: ReactNode
}

function Model({children}: Props) {
    return (<>
        <div className={classes.backdrop}>
            <div className={classes.wrapper}>
                {children}
            </div>
        </div>

    </>)

}

export default Model
import classes from './Model.module.css'

function Model(props) {
    const {children} = props;
    return (<>
        <div className={classes.backdrop}>
            <dialog className={classes.modal}>
                {children}
            </dialog>
        </div>
    </>)

}

export default Model
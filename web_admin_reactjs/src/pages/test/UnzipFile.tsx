import { Button } from "antd";

const UnzipFile: React.FC = () => {


    const uploadzip = () => {
        const inputTag = document.createElement('input');
        inputTag.setAttribute('type', 'file');
        inputTag.setAttribute('accept', '.zip');
        inputTag.click();
        inputTag.onchange = (e) => {
            const file = (e.target as HTMLInputElement).files![0];
            console.log('file: ', file);
            unzip(file)
        }
    }    
    
    const unzip = (file: File) => {

    }

    return <>
        unzip page
        <Button onClick={uploadzip}>Upload zip</Button>
    </>
}

export default UnzipFile;
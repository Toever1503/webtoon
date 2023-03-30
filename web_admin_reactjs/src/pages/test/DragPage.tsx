import { Button } from "antd";
import { useEffect } from "react";
import Sortable from 'sortablejs';

const DragPage: React.FC = () => {
    var sortable: Sortable;

    const getData = () => {
        console.log('data', sortable.toArray());
    }

    const downloadFile = () => {
        var parts = [
            new Blob(['you construct a file...'], { type: 'image/png' }),
            ' Same way as you do with blob',
            new Uint16Array([33])
        ];

        // Construct a file
        var file = new File(parts, 'sample.txt', {
            // @ts-ignore
            lastModified: new Date(0), // optional - default = now
            type: "image/png" // optional - default = ''
        });

        var fr = new FileReader();

        fr.onload = function (evt) {
            // @ts-ignore
            //  document.body.innerHTML = evt.target.result + "<br><a href="+URL.createObjectURL(file)+" download=" + file.name + ">Download " + file.name + "</a><br>type: "+file.type+"<br>last modified: "+ file.lastModifiedDate
            console.log('file: ', file);

        }

        fr.readAsText(file);
    }
    useEffect(() => {
        var el: HTMLElement | null = document.getElementById('items');
        if (el)
            sortable = Sortable.create(el, {
                swapThreshold: 1,
                animation: 150,
            });
    }, []);

    let data = [
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://th.bing.com/th?id=OSK.HEROGLit0G7C_sVAzGZYoabO-Xaih239ffzWh-HxYpS8coI&w=472&h=280&c=1&rs=2&o=6&pid=SANGAM",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
        "https://picsum.photos/200/300",
    ]
    return (<>
        drag

        <div id="items" className="flex flex-wrap gap-[5px]">
            {data.map((item, index) => (
                <div data-id="1a" className="w-[102px] h-[120px]">
                    <img className="h-full w-full" src={item} key={index} />
                </div>
            ))}
        </div>

        <Button onClick={getData}>Get data</Button>
        <Button onClick={downloadFile}>Download file</Button>
    </>)
}

export default DragPage;
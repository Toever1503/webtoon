
import { Count, FontFamilyModel, HtmlEditor, Image, Inject, Link, QuickToolbar, RichTextEditorComponent, Table, Toolbar, ToolbarSettingsModel, ToolbarType } from '@syncfusion/ej2-react-richtexteditor';
import { useEffect, useRef, useState } from 'react';


interface RichtextEditorFormProps {
    onReady: Function,
    toolbarSettings?: ToolbarSettingsModel
}

const RichtextEditorForm: React.FC<RichtextEditorFormProps> = (props: RichtextEditorFormProps) => {

    const fontFamily: FontFamilyModel = {
        items: [
            { text: "Segoe UI", value: "Segoe UI", cssClass: "e-segoe-ui", command: "Font", subCommand: "FontName" },
            { text: "Roboto", value: "Roboto", command: "Font", subCommand: "FontName" }, // here font is added
            { text: "Great vibes", value: "Great Vibes,cursive", command: "Font", subCommand: "FontName" }, // here font is added
            { text: "Noto Sans", value: "Noto Sans", command: "Font", subCommand: "FontName" },
            { text: "Impact", value: "Impact,Charcoal,sans-serif", cssClass: "e-impact", command: "Font", subCommand: "FontName" },
            { text: "Tahoma", value: "Tahoma,Geneva,sans-serif", cssClass: "e-tahoma", command: "Font", subCommand: "FontName" },
        ]
    };

    const toolbarSettings: ToolbarSettingsModel = {
        type: ToolbarType.Expand,
        enableFloating: true,
        items: ['Bold', 'Italic', 'Underline', 'StrikeThrough',
            'FontName', 'FontSize', 'FontColor', 'BackgroundColor',
            'LowerCase', 'UpperCase', '|',
            'Formats', 'Alignments', 'OrderedList', 'UnorderedList',
            'Outdent', 'Indent', '|',
            'CreateLink', {
                tooltipText: 'Insert Image',
                undo: true,
                click: () => {
                    console.log('insert image');

                    //   console.log(rteInstance.value);
                    //   fileModalPopup.open((files) => {
                    //     console.log('avatar: ', files);
                    //     const imgTag = `<p>
                    //       <img src="https://i.ytimg.com/vi/0k5GI2ZK87s/maxresdefault.jpg"
                    //        class="e-rte-image e-imginline e-resize" alt="Bread.png" width="auto" height="auto" style="min-width: 0px; max-width: 855px; min-height: 0px;">
                    //        </p>`;

                    //     if (!postModel.content)
                    //       postModel.content = postModel.content + imgTag;
                    //     else
                    //       postModel.content = imgTag;
                    //   })
                },
                template: "<button class=\"e-tbar-btn e-btn\" tabindex=\"-1\" style=\"width:100%\">" +
                    "<span class='e-btn-icon e-image e-icons' style='font-weight: 500;'></span>" +
                    "</button>"
            }, '|',
            'ClearFormat', 'Print',
            'SourceCode', 'FullScreen', '|', 'Undo', 'Redo', '|',
            'CreateTable', 'TableRows', 'TableColumns', '-', 'TableHeader', 'TableCellVerticalAlign', 'TableRemove', 'Styles', '|',
        ]
    };


    const [content, setContent] = useState<string>('a');

    useEffect(() => {
        console.log('richtext editor mounted');
    }, []);


    return (
        <div className="richtext-editor w-full">
            <RichTextEditorComponent className='min-h-[360px] w-full' height={'100%'} value={content} ref={(richtexteditor: RichTextEditorComponent) => props.onReady(richtexteditor, setContent)}
                toolbarSettings={props.toolbarSettings || toolbarSettings} fontFamily={fontFamily}>

                <Inject services={[Toolbar, Image, Link, HtmlEditor, Count, Table, QuickToolbar]} />
            </RichTextEditorComponent>
        </div>)
};

export default RichtextEditorForm;
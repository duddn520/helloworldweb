function strToHTML(str){
    const parser = new DOMParser();
    const doc = parser.parseFromString(str, 'text/html')
    const element = doc.body.childNodes;
    return element;
}

export default strToHTML;
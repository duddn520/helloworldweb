/**
 * base64 를 Blob 오브젝트로 만드는 함수
 * @param { String } b64Data
 * @param { String } contentType mimeType
 * @param { Number } sliceSize 쪼개는 사이즈
 */
function b64toBlob (b64Data, contentType = '', sliceSize = 512) {
    if (b64Data === '' || b64Data === undefined) return

    const byteCharacters = atob(b64Data)
    const byteArrays = []

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
    const slice = byteCharacters.slice(offset, offset + sliceSize)
    const byteNumbers = new Array(slice.length)
    for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i)
    }
    const byteArray = new Uint8Array(byteNumbers)
    byteArrays.push(byteArray)
    }

    const blob = new Blob(byteArrays, { type: contentType })
    return blob
}

/**
 * 파일을 Base64 형식에서 > File 형식으로 (디코딩)변환합니다
 * @param { String } image Base64 형식의 String
 * @param { String } fileName Base64 에서는 파일 명을 저장할 수 없습니다.
 */
function convertBase64IntoFile (image, fileName) {
    const mimeType = image?.match(/[^:]\w+\/[\w-+\d.]+(?=;|,)/)[0]   // image/jpeg
    const realData = image.split(',')[1]   // 이 경우에선 /9j/4AAQSkZJRgABAQAAAQABAAD...

    const blob = b64toBlob(realData, mimeType)
    const raw = new File([blob], fileName, { type: mimeType })
    
    return raw;
}

function extractOnlyFilename(original){
    let stringArray = original.split(".")
    return original.replace("."+stringArray[ stringArray.length -1 ], "");
}

export {
    convertBase64IntoFile, extractOnlyFilename
}
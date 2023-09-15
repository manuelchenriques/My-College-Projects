// The following handlers can be outside the component because they dont use or change state
import React from "react";

export function handleDragStart(event: React.DragEvent<HTMLDivElement>) {
    const ix = event.currentTarget.attributes.getNamedItem('data-ix').textContent
    console.log(`dragStart - ${ix}`)
    event.dataTransfer.effectAllowed = "all"
    event.dataTransfer.setData("text/plain", ix)
}

export function handleDragOver(event: React.DragEvent<HTMLDivElement>) {
    event.preventDefault()
    console.log("dragOver")
    event.dataTransfer.dropEffect = "copy";
}
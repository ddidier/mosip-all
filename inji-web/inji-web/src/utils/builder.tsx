import React from 'react';

export const renderContent = (content: string | { __html: string }) => {
    if (typeof content === 'object' && '__html' in content) {
        return (
            <span 
                dangerouslySetInnerHTML={{
                    __html: content.__html.replace(
                        /<a\s/g, 
                        '<a class="text-blue-600 hover:text-blue-800 underline font-semibold" '
                    )
                }} 
            />
        );
    }
    return content;
};

export const renderGradientText = (title:string) => {
    return <span className="bg-gradient-to-r from-iw-primary to-iw-secondary bg-clip-text text-md font-bold text-transparent">{title}</span>
}

export const constructContent = (descriptions: string[],applyHTML:boolean) => {
    return descriptions.map((desc, index) => {
        if (applyHTML) {
            return { __html: desc };
        }
        return desc;
    });
};

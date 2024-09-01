import { createGlobalStyle } from "styled-components";
import { AppTheme } from "./AppTheme";

function MarginPaddingInline() {
  return Object.entries(AppTheme.XAxisSpacing)
    .map((field) => {
      const key = field[0].toLowerCase();
      const value = field[1];
      return `
    ${toClassName(`ml-${key}`, `margin-left:${value}`)}
    ${toClassName(`mr-${key}`, `margin-right:${value}`)}
    ${toClassName(`pl-${key}`, `padding-left:${value}`)}
    ${toClassName(`pr-${key}`, `padding-right:${value}`)}
    `;
    })
    .join("");
}
function MarginPaddingBlock() {
  return Object.entries(AppTheme.YAxisSpacing)
    .map((field) => {
      const key = field[0].toLowerCase();
      const value = field[1];
      return `
    ${toClassName(`mt-${key}`, `margin-top:${value}`)}
    ${toClassName(`mb-${key}`, `margin-bottom:${value}`)}
    ${toClassName(`pt-${key}`, `padding-top:${value}`)}
    ${toClassName(`pb-${key}`, `padding-bottom:${value}`)}
    `;
    })
    .join("");
}
function MarginPadding() {
  return Object.entries(AppTheme.Surroundings)
    .map((field) => {
      const key = field[0].toLowerCase();
      const value = field[1];
      return `
    ${toClassName(`m-${key}`, `margin:${value}`)}
    ${toClassName(`p-${key}`, `padding:${value}`)}
    `;
    })
    .join("");
}
function toClassName(name: string, value: string): string {
  return `.${name}{
        ${value};
    }`;
}
function Flex() {
  return `
    ${toClassName("flex", "display:flex")}
    ${toClassName("row", "flex-direction:row")}
    ${toClassName("col", "flex-direction:column")}
    ${toClassName("align-items-center", "align-items:center")}
    ${toClassName("align-items-end", "align-items:end")}
    ${toClassName("align-items-start", "align-items:start")}
    ${toClassName("j-content-center", "justify-content:center")}
    ${toClassName("j-content-between", "justify-content:space-between")}
    ${toClassName("j-content-evenly", "justify-content:space-evenly")}
    ${toClassName("j-content-around", "justify-content:space-around")}
    `;
}
export const GlobalStyle = createGlobalStyle`
    ${MarginPaddingInline()}
    ${MarginPaddingBlock()}
    ${MarginPadding()}
    ${Flex()}
`;
